import React, { useState, useEffect, useCallback, useRef } from 'react';
import '../styles/MainContentStyles.css';
import SideBar from './SideBar';
import ContentFeed from './ContentFeed';
import SideBarRight from './SideBarRight';
import CreatePollComponent from './CreatePollComponent';
import getContentFeedRequest from '../../../services/ApiRequests/FeedRequest';

const MainContent = ({ followedTags, feedType, param, setFollowedTags, userData }) => {
    const [contentFeed, setContentFeed] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isCommentsLoading, setIsCommentsLoading] = useState(false);
    const [feedOverCreatePoll, setFeedOverCreatePoll] = useState(true);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [searchedUserData, setSearchedUserData] = useState({});
    const [comments, setComments] = useState([]);
    const [commentsPage, setCommentsPage] = useState(0);
    const [hasMoreComments, setHasMoreComments] = useState(true);
    const limitForFeed = 4;
    const limitForComments = 5;
    const initialFetchDone = useRef(false);
    const [isInitialFetchHappening, setIsInitialFetchHappening] = useState(false);

    const fetchFeed = useCallback(async (feedType, param, currentPage, limit, isLoadMore = false) => {
        if (
            (feedType === "tag" && !param) ||
            (feedType === "user" && !param) ||
            (feedType === "poll" && !param) ||
            (feedType === "home" && followedTags.length === 0)
        ) {
            return;
        }

        try {
            setIsLoading(true);
            const response = await getContentFeedRequest(feedType, param, currentPage, limit);
            if (response.status >= 200 && response.status < 300) {
                const newData = response.data.data || [];
                setContentFeed(prev => (isLoadMore ? [...prev, ...newData] : newData));
                setHasMore(newData.length === limit);
            } else {
                console.error('Error fetching content feed:', response);
                window.alert('Failed to fetch the content feed. Please try again later.');
            }
        } catch (error) {
            console.error('Fetch Feed Error:', error);
            window.alert('An error occurred while fetching the content feed.');
        } finally {
            setIsLoading(false);
        }
    }, [followedTags]);

    const fetchComments = useCallback(async (pollId, currentPage, limit, isLoadMore = false) => {
        if (!pollId || isCommentsLoading) return;

        try {
            setIsCommentsLoading(true);
            const response = await getContentFeedRequest("commentsForPoll", pollId, currentPage, limit);
            if (response.status >= 200 && response.status < 300) {
                const newComments = response.data.data || [];
                setComments(prev => (isLoadMore ? [...prev, ...newComments] : newComments));
                setHasMoreComments(newComments.length === limit);
            } else {
                console.error('Error fetching comments:', response);
                window.alert('Failed to fetch comments. Please try again later.');
            }
        } catch (error) {
            console.error('Fetch Comments Error:', error);
            window.alert('An error occurred while fetching comments.');
        } finally {
            setIsCommentsLoading(false);
        }
    }, []);

    const handleScroll = useCallback(() => {
        if (isLoading || isCommentsLoading) return;

        const middlePartElement = document.querySelector('.middlePartOfThePage');
        const { scrollTop, scrollHeight, clientHeight } = middlePartElement;

        if (scrollTop + clientHeight >= scrollHeight - 1) {
            if (feedType === "poll" && hasMoreComments) {
                setCommentsPage(prev => {
                    const nextPage = prev + 1;
                    fetchComments(param, nextPage, limitForComments, true);
                    return nextPage;
                });
            } else if (hasMore) {
                setPage(prev => {
                    const nextPage = prev + 1;
                    fetchFeed(feedType, param, nextPage, limitForFeed, true);
                    return nextPage;
                });
            }
        }
    }, [isLoading, isCommentsLoading, hasMore, commentsPage, hasMoreComments, page, feedType, param, fetchFeed, fetchComments]);

    useEffect(() => {
        if (isInitialFetchHappening) return;
        setIsInitialFetchHappening(true);

        if (feedType === "home" && followedTags.length === 0) {
            setIsInitialFetchHappening(false);
            return;
        }

        setPage(0);
        setContentFeed([]);
        setComments([]);
        setCommentsPage(0);
        setHasMoreComments(true);

        fetchFeed(feedType, param, 0, limitForFeed);

        if (feedType === "tag" || feedType === "home" || feedType === "popular") {
            setIsInitialFetchHappening(false);
        }

        if (feedType === "user") {
            getContentFeedRequest("justBasicUserDataWhenSearched", param).then(response => {
                if (response.status >= 200 && response.status < 300) {
                    setSearchedUserData(response.data.data);
                } else {
                    window.alert(response.status === 404 ? "User not found" : "Failed to fetch user data. Please try again later.");
                }
                setIsInitialFetchHappening(false);
            });
        }

        if (feedType === "poll") {
            fetchComments(param, 0, limitForComments);
            setIsInitialFetchHappening(false);
        }
    }, [feedType, param, followedTags, fetchFeed, fetchComments]);

    useEffect(() => {
        if (initialFetchDone.current) return;
        fetchFeed(feedType, param, page, limitForFeed);
        initialFetchDone.current = true;
    }, [fetchFeed, feedType, param, page, limitForFeed]);

    useEffect(() => {
        const middlePartElement = document.querySelector('.middlePartOfThePage');
        middlePartElement.addEventListener('scroll', handleScroll);
        return () => middlePartElement.removeEventListener('scroll', handleScroll);
    }, [handleScroll]);

    return (
        <div className='mainContentContainer' style={{ display: 'flex', flexDirection: 'row', height: '90%', width: '100%', padding: '0', margin: '0' }}>
            <SideBar followedTags={followedTags} setIsFeedOverCreatePoll={setFeedOverCreatePoll} isFeedOverCreatePoll={feedOverCreatePoll} />
            <div className='middlePartOfThePage'>
                {feedOverCreatePoll ? (
                    <ContentFeed
                        contentFeed={contentFeed}
                        feedType={feedType}
                        followedTags={followedTags}
                        param={param}
                        isLoading={isLoading}
                        setFollowedTags={setFollowedTags}
                        searchedUserData={searchedUserData}
                        hasMore={hasMore}
                        comments={comments}
                        userData={userData}
                        setComments={setComments}
                        isCommentsLoading={isCommentsLoading}
                    />
                ) : (
                    <CreatePollComponent />
                )}
            </div>
            <SideBarRight feedOverCreatePoll={feedOverCreatePoll} setFeedOverCreatePoll={setFeedOverCreatePoll} />
        </div>
    );
};

export default MainContent;