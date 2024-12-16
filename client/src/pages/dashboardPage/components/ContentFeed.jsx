import React, { useEffect, useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';
import IndividualCommentComponent from './IndividualCommentComponent';
import followOrUnfollowTagsRequest from '../../../services/ApiRequests/followOrUnfollowTagsRequest';
import axiosInstanceWithNoErrorChecks from '../../../services/axiosInstanceWithNoErrorChecks';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading, setFollowedTags, searchedUserData, hasMore, comments, userData, setComments }) => {

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

    const [commentInputState, setCommentInputState] = useState("");

    function handleCommentInputChange(e) {
        setCommentInputState(e.target.value);
    }

    async function handleCommentSubmit() {
        try {
            const response = await axiosInstanceWithNoErrorChecks.post(`/poll/addComment/${param}`, { comment: commentInputState });
            if (response.status >= 200 && response.status < 300) {
                const newComment = response.data.data;
                setComments((prevComments) => [...prevComments, newComment]);
                setCommentInputState("");
            } else {
                window.alert('Error Adding Comment');
            }
        } catch (error) {
            console.log(error);
            window.alert('Error Adding Comment');
        }
    }


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
                            {!hasMore && feedType != "poll" && (
                                <h2 className="noMoreContentMessage">
                                    No more content available. Check back later!
                                </h2>
                            )}
                        </>
                    ) : (
                        isLoading ? (
                            <h2 className="loadingMessage">
                                Loading...
                            </h2>
                        ) : (
                            feedType === "poll" && contentFeed.length > 0 ? null : (
                                <h2 className="noContentMessage">No content available. Check back later!</h2>
                            )
                        )
                    )}
                    {feedType === "poll" && contentFeed.length > 0 && (
                        <>
                            <div className='enterCommentDiv'>
                                <img src={userData.profilePictureURL} />
                                <input placeholder='Enter a comment' value={commentInputState} onChange={handleCommentInputChange} />
                                <svg width="30" height="30" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg" transform='rotate(45) translate(-2, 5)' style={{ cursor: 'pointer' }} onClick={handleCommentSubmit}>
                                    <path d="M15.8492 23.3176L21.5486 33.4831C22.247 34.7288 24.0864 34.5765 24.5704 33.2328L33.8622 7.43744C34.3402 6.11038 33.0564 4.82655 31.7293 5.30457L5.93393 14.5964C4.59026 15.0804 4.43794 16.9197 5.68369 17.6182L15.8492 23.3176ZM15.8492 23.3176L19.5943 19.5725" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                                </svg>
                            </div>
                            <div className='allCommentsContainer'>
                                {comments.map((comment) => (<IndividualCommentComponent key={comment.id} comment={comment} />))}
                            </div>
                        </>
                    )
                    }
                </>
            )}
        </div>
    );
};

export default ContentFeed;
