import React, { useState, useEffect } from 'react'
import '../styles/MainContentStyles.css'
import SideBar from './SideBar'
import ContentFeed from './ContentFeed'
import SideBarRight from './SideBarRight'
import CreatePollComponent from './CreatePollComponent'
import getContentFeedRequest from '../../../services/ApiRequests/getContentFeedRequest'

const MainContent = ({ followedTags, feedType, param }) => {

    const [contentFeed, setContentFeed] = useState([]);

    const [feedOverCreatePoll, setFeedOverCreatePoll] = React.useState(true);

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
        console.log("Fetching feed for", feedType, param);
        const response = fetchFeed(feedType, param, 0, 3).then((response) => {
            setContentFeed(response);
        })
    }, [feedType, param]);

    return (
        <div className='mainContentContainer' style={{ display: 'flex', flexDirection: 'row', height: '90%', width: '100%', padding: '0', margin: '0' }}>
            <SideBar followedTags={followedTags} />
            <div className='middlePartOfThePage'>
                {feedOverCreatePoll ? <ContentFeed contentFeed={contentFeed} feedType={feedType} followedTags={followedTags} /> : <CreatePollComponent />}
            </div>
            <SideBarRight feedOverCreatePoll={feedOverCreatePoll} setFeedOverCreatePoll={setFeedOverCreatePoll} />
        </div>
    )
}

export default MainContent