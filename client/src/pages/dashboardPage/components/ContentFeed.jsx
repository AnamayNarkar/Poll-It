import React from 'react'
import '../styles/ContentFeedStyles.css'
import getContentFeedRequest from '../../../services/ApiRequests/getContentFeedRequest'

const ContentFeed = ({ feedType, param, followedTags }) => {



    return (
        <div className='contentFeedContainer'>
            {feedType === "home" && followedTags.length === 0 ? (
                <h1>Follow some tags to see content</h1>
            ) : (<>
                <h1>Content Feed</h1>
            </>
            )}
        </div>
    )
}

export default ContentFeed
