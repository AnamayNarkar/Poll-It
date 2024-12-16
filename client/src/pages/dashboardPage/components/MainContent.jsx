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
    const [feedOverCreatePoll, setFeedOverCreatePoll] = useState(true);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [searchedUserData, setSearchedUserData] = useState({});
    const [comments, setComments] = useState([{
        "id": "675fd47a1aa25b421269c7cc",
        "pollId": "675b616c482ac964b68c60d3",
        "username": "chicken",
        "profilePictureURL": "https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png",
        "comment": "good poll"
    }]);
    const limit = 4;
    const initialFetchDone = useRef(false);

    const [areFollowedTagsLoaded, setAreFollowedTagsLoaded] = useState(false);

    const fetchFeed = useCallback(

        async (feedType, param, currentPage, limit, isLoadMore = false) => {

            if ((feedType == "tag" || feedType == "user" || feedType == "poll") && !param) {
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
        },
        []
    );

    const handleScroll = useCallback(() => {
        if (isLoading || !hasMore) return;

        const middlePartElement = document.querySelector('.middlePartOfThePage');
        const { scrollTop, scrollHeight, clientHeight } = middlePartElement;
        if (scrollTop + clientHeight >= scrollHeight - 1) {
            const nextPage = page + 1;
            setPage(nextPage);
            fetchFeed(feedType, param, nextPage, limit, true);
        }
    }, [isLoading, hasMore, page, feedType, param, fetchFeed]);

    useEffect(() => {
        setAreFollowedTagsLoaded(true);
    }, [followedTags])

    useEffect(() => {

        setFeedOverCreatePoll(true);

        if (feedType === "home" && followedTags.length === 0) {
            return;
        }

        setPage(0);
        setContentFeed([]);
        fetchFeed(feedType, param, 0, limit);

        if (feedType === "user") {
            getContentFeedRequest("justBasicUserDataWhenSearched", param).then((response) => {
                if (response.status >= 200 && response.status < 300) {
                    setSearchedUserData(response.data.data);
                } else {
                    if (response.status === 404) {
                        window.alert("User not found");
                    } else {
                        window.alert("Failed to fetch user data. Please try again later.");
                    }
                }
            });
        }

    }, [feedType, param, areFollowedTagsLoaded]);

    useEffect(() => {
        const middlePartElement = document.querySelector('.middlePartOfThePage');
        middlePartElement.addEventListener('scroll', handleScroll);
        return () => middlePartElement.removeEventListener('scroll', handleScroll);
    }, [handleScroll]);

    useEffect(() => {

        const shouldFetchFeed = () => {
            if (initialFetchDone.current) return false;
            if (feedType === "poll" && param) {
                return true;
            }
            if ((feedType === "home" || feedType === "tag" || feedType === "user") && followedTags.length > 0 && param) {
                return true;
            }
            return false;
        };

        if (shouldFetchFeed()) {
            fetchFeed(feedType, param, page, limit);
            initialFetchDone.current = true;
        }
    }, [fetchFeed, feedType, param, page, limit, followedTags]);


    return (
        <div
            className='mainContentContainer'
            style={{
                display: 'flex',
                flexDirection: 'row',
                height: '90%',
                width: '100%',
                padding: '0',
                margin: '0',
            }}
        >
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
                    />
                ) : (
                    <CreatePollComponent />
                )}
            </div>
            <SideBarRight
                feedOverCreatePoll={feedOverCreatePoll}
                setFeedOverCreatePoll={setFeedOverCreatePoll}
            />
        </div>
    );
};

export default MainContent;