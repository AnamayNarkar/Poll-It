import React, { useEffect, useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';
import getContentFeedRequest from '../../../services/ApiRequests/FeedRequest';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading, ifFeedTypeIsUserThenUserDataWithAFewPolls }) => {
    const [page, setPage] = useState(1);

    const [searchedUserData, setSearchedUserData] = useState({});

    useEffect(() => {

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

    }, []);

    return (
        <div className='contentFeedContainer'>
            {feedType === 'home' && followedTags.length === 0 ? (
                <h1>Follow some tags to see content</h1>
            ) : (
                <>
                    {feedType === 'tag' && (
                        <div className='tagSearchTopBarToFollow' >
                            <h2>Tag: {param}</h2>
                        </div>
                    )}
                    {feedType === 'user' && (
                        <div className='userSearchTopBar'>
                            <h2>{searchedUserData.username}</h2>
                        </div>
                    )}
                    {contentFeed.length > 0 ? (
                        <>
                            {contentFeed.map((poll, index) => (
                                <IndividualPollComponent key={index} poll={poll} />
                            ))}
                            {isLoading && <h2 style={{ color: 'white' }}>Loading...</h2>}
                        </>
                    ) : (
                        <h2>No content available. Check back later!</h2>
                    )}
                </>
            )}
        </div>
    );
};

export default ContentFeed;