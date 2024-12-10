import React, { useState } from 'react';
import '../styles/ContentFeedStyles.css';
import IndividualPollComponent from './IndividualPollComponent';

const ContentFeed = ({ contentFeed = [], feedType, followedTags = [], param, isLoading }) => {
    const [page, setPage] = useState(1);

    return (
        <div className='contentFeedContainer'>
            {feedType === 'home' && followedTags.length === 0 ? (
                <h1>Follow some tags to see content</h1>
            ) : (
                <>
                    {feedType === 'tag' && (
                        <div className='tagSearchTopBarToFollow'>
                            <h2>Tag: {param}</h2>
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