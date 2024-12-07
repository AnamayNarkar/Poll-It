import React, { useEffect, useState } from 'react'
import '../styles/ContentFeedStyles.css'
import getContentFeedRequest from '../../../services/ApiRequests/getContentFeedRequest'
import IndividualPollComponent from './IndividualPollComponent';

const ContentFeed = ({ feedType, param = "", followedTags }) => {

    const [page, setPage] = useState(1);

    const [contentFeed, setContentFeed] = useState([]);

    const [response, setResponse] = useState({});

    const fetchFeed = async (feedType, param, page, limit) => {
        const response = await getContentFeedRequest(feedType, param, page, limit);
        console.log(response.status);
        if (response.status >= 200 && response.status < 300) {
            return response.data.data;
        } else {
            window.alert("Error fetching content feed");
        }
    }

    useEffect(() => {
        const response = fetchFeed(feedType, param, page, 10).then((response) => {
            setContentFeed(response);

        })
        console.log(contentFeed);
    }, [])

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
