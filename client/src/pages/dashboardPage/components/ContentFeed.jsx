import React, { useEffect, useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';
import followOrUnfollowTagsRequest from '../../../services/ApiRequests/followOrUnfollowTagsRequest';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading, setFollowedTags, searchedUserData, hasMore }) => {
    const [page, setPage] = useState(1);

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
    };

    const string = "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Culpa doloribus quis repellendus quibusdam repudiandae minus numquam vero similique id! Ut quasi rem debitis magni ipsam dolore exercitationem accusantium. Amet, similique?";

    return (
        <div className='contentFeedContainer'>
            {feedType === 'home' && followedTags.length === 0 ? (
                <h2 style={{
                    color: 'white',
                    fontFamily: 'Parkinsans sans-serif',
                    textAlign: 'center'
                }}>Follow some tags to see content</h2>
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
                                fontFamily: "'Lato', sans-serif",
                                margin: '0',
                                fontSize: '1.5rem',
                                marginLeft: '50px',
                            }}>
                                t/{param}
                            </h2>
                            <button onClick={() => followOrUnfollowTag(param)}
                                style={{
                                    backgroundColor: 'transparent',
                                    color: 'white',
                                    fontSize: '1.5rem',
                                    marginRight: '50px',
                                    fontFamily: "'Parkinsans', sans-serif",
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
                            <div className="userDataWhenSearchedLeftPart">
                                <img className='userDataWhenSearchedProfilePic' src='https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png' />
                            </div>
                            <div className='userDataWhenSearchedRightPart'>
                                <h2 className='userDataWhenSearchedUsername'>u/{searchedUserData.username}</h2>
                                <p className='userDataWhenSearchedBio'>{string}</p>
                            </div>
                        </div>
                    )}
                    {contentFeed.length > 0 ? (
                        <>
                            {contentFeed.map((poll, index) => (
                                <IndividualPollComponent key={index} poll={poll} />
                            ))}
                            {isLoading && (
                                <h2 style={{
                                    color: 'white',
                                    fontFamily: 'Parkinsans sans-serif',
                                    textAlign: 'center'
                                }}>Loading...</h2>
                            )}
                            {!hasMore && (<h2 style={{
                                color: 'white',
                                fontFamily: 'Parkinsans sans-serif',
                                textAlign: 'center'
                            }}>
                                No more content available. Check back later!
                            </h2>)}
                        </>
                    ) : (
                        isLoading ? (
                            <h2 style={{
                                color: 'white',
                                fontFamily: 'Parkinsans sans-serif',
                                textAlign: 'center'
                            }}>
                                Loading...
                            </h2>
                        ) : (
                            <h2 style={{
                                color: 'white',
                                fontFamily: 'Parkinsans sans-serif',
                                textAlign: 'center'
                            }}>No content available. Check back later!</h2>
                        )
                    )}
                </>
            )}
        </div>
    );
};

export default ContentFeed;
