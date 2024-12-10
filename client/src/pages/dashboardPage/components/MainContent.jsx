import React, { useState, useEffect, useCallback } from 'react';
import '../styles/MainContentStyles.css';
import SideBar from './SideBar';
import ContentFeed from './ContentFeed';
import SideBarRight from './SideBarRight';
import CreatePollComponent from './CreatePollComponent';
import getContentFeedRequest from '../../../services/ApiRequests/FeedRequest';

const MainContent = ({ followedTags, feedType, param, setFollowedTags }) => {
    const [contentFeed, setContentFeed] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [feedOverCreatePoll, setFeedOverCreatePoll] = useState(true);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const limit = 4;

    const fetchFeed = useCallback(
        async (feedType, param, currentPage, limit, isLoadMore = false) => {
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
        console.log("scrolling");
        if (isLoading || !hasMore) return;

        const middlePartElement = document.querySelector('.middlePartOfThePage');
        const { scrollTop, scrollHeight, clientHeight } = middlePartElement;
        if (scrollTop + clientHeight >= scrollHeight - 1) {
            console.log("fetching")
            const nextPage = page + 1;
            setPage(nextPage);
            fetchFeed(feedType, param, nextPage, limit, true);
        }
    }, [isLoading, hasMore, page, feedType, param, fetchFeed]);

    useEffect(() => {
        setPage(0);
        setContentFeed([]);
        fetchFeed(feedType, param, 0, limit);
    }, [feedType, param, fetchFeed]);

    useEffect(() => {
        const middlePartElement = document.querySelector('.middlePartOfThePage');
        middlePartElement.addEventListener('scroll', handleScroll);
        return () => middlePartElement.removeEventListener('scroll', handleScroll);
    }, [handleScroll]);

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
            <SideBar followedTags={followedTags} />
            <div className='middlePartOfThePage'>
                {feedOverCreatePoll ? (
                    <ContentFeed
                        contentFeed={contentFeed}
                        feedType={feedType}
                        followedTags={followedTags}
                        param={param}
                        isLoading={isLoading}
                        setFollowedTags={setFollowedTags}
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