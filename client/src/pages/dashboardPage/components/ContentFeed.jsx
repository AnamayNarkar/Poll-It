import React, { useEffect, useState } from 'react'
import '../styles/ContentFeedStyles.css'
import IndividualPollComponent from './IndividualPollComponent';

const ContentFeed = ({ contentFeed, feedType, followedTags }) => {

    const [page, setPage] = useState(1);

    return (
        <div className='contentFeedContainer'>
            {feedType === "home" && followedTags.length === 0 ? (
                <h1>Follow some tags to see content</h1>
            ) : (<>
                {contentFeed && contentFeed.length > 0 &&
                    contentFeed.map((poll, index) => {
                        return <IndividualPollComponent key={index} poll={poll} />
                    })}
            </>
            )}
        </div>
    )
}

export default ContentFeed
