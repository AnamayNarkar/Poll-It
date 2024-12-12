import React, { useEffect, useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';
import followOrUnfollowTagsRequest from '../../../services/ApiRequests/followOrUnfollowTagsRequest';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading, setFollowedTags, searchedUserData, hasMore }) => {

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

    return (
        <div className='contentFeedContainer'>
            {feedType === 'home' && followedTags.length === 0 ? (
                <h2 className="followTagsMessage">Follow some tags to see content</h2>
            ) : (
                <>
                    {feedType === 'tag' && (
                        <div className='tagSearchTopBarToFollow'>
                            <h2 className="tagTitle">
                                t/{param}
                            </h2>
                            <button onClick={() => followOrUnfollowTag(param)} className="followButton">
                                {followedTags.map((tag) => tag.name).includes(param) ? 'Unfollow' : 'Follow'}
                            </button>
                        </div>
                    )}
                    {feedType === 'user' && searchedUserData && searchedUserData.username ? (
                        <div className='userSearchTopBar'>
                            <div className="userDataWhenSearchedLeftPart">
                                <img className='userDataWhenSearchedProfilePic' src={searchedUserData.profilePictureURL} alt="Profile" />
                            </div>
                            <div className='userDataWhenSearchedRightPart'>
                                <h2 className='userDataWhenSearchedUsername'>u/{searchedUserData.username}</h2>
                                <p className='userDataWhenSearchedBio'>{searchedUserData.bio}</p>
                            </div>
                        </div>
                    ) : null}

                    {contentFeed.length > 0 ? (
                        <>
                            {contentFeed.map((poll, index) => (
                                <IndividualPollComponent key={index} poll={poll} />
                            ))}
                            {isLoading && (
                                <h2 className="loadingMessage">Loading...</h2>
                            )}
                            {!hasMore && (<h2 className="noMoreContentMessage">
                                No more content available. Check back later!
                            </h2>)}
                        </>
                    ) : (
                        isLoading ? (
                            <h2 className="loadingMessage">
                                Loading...
                            </h2>
                        ) : (
                            <h2 className="noContentMessage">No content available. Check back later!</h2>
                        )
                    )}
                </>
            )}
        </div>
    );
};

export default ContentFeed;
