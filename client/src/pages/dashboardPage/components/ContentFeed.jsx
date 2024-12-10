import React, { useEffect, useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';
import getContentFeedRequest from '../../../services/ApiRequests/FeedRequest';
import followOrUnfollowTagsRequest from '../../../services/ApiRequests/followOrUnfollowTagsRequest';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading, setFollowedTags }) => {
    const [page, setPage] = useState(1);

    const [searchedUserData, setSearchedUserData] = useState({});

    const followOrUnfollowTag = async (tagName) => {
        try {
            const response = await followOrUnfollowTagsRequest(tagName);
            if (response.status >= 200 && response.status < 300) {
                if (followedTags.map((tag) => tag.name).includes(tagName)) {
                    setFollowedTags(followedTags.filter((tag) => tag.name !== tagName));
                } else {
                    setFollowedTags([...followedTags, {
                        name: response.data.data.name,
                        id: response.data.data.id
                    }]);
                }
            } else {
                window.alert('Error following/unfollowing tag');
            }
        } catch (error) {
            window.alert('Error following/unfollowing tag');
        }
    }

    useEffect(() => {

        if (feedType === "user") {
            getContentFeedRequest("justBasicUserDataWhenSearched", param).then((response) => {
                if (response.status >= 200 && response.status < 300) {
                    setSearchedUserData(response.data.data);
                    console.log(searchedUserData);
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
                        <div className='tagSearchTopBarToFollow' style={{
                            backgroundColor: 'rgb(60, 60, 55)',
                            height: '50px',
                            width: '100%',
                            margin: '0',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'space-between',
                            position: 'relative',
                        }}>
                            <h2 style={{
                                color: 'white',
                                fontFamily: 'Parkinsans ',
                                margin: '0',
                                fontSize: '1.5rem',
                                marginLeft: '50px',
                            }}
                            >t/{param}</h2>
                            <button onClick={() => {
                                followOrUnfollowTag(param);
                            }}
                                style={{
                                    backgroundColor: 'transparent',
                                    border: 'none',
                                    color: 'white',
                                    fontSize: '1.5rem',
                                    marginRight: '50px',
                                    fontFamily: 'Parkinsans',
                                    borderRadius: '10px',
                                    border: '1px solid #3a3a3a',
                                    cursor: 'pointer',
                                }}>
                                {followedTags.map((tag) => tag.name).includes(param) ? 'Unfollow' : 'Follow'}
                            </button>
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