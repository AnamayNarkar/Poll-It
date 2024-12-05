import React from 'react'
import '../styles/MainContentStyles.css'
import SideBar from './SideBar'
import ContentFeed from './ContentFeed'
import SideBarRight from './SideBarRight'
import CreatePollComponent from './CreatePollComponent'

const MainContent = () => {

    const [feedOverCreatePoll, setFeedOverCreatePoll] = React.useState(true);

    return (
        <div className='mainContentContainer' style={{ display: 'flex', flexDirection: 'row', height: '90%', width: '100%', padding: '0', margin: '0' }}>
            <SideBar />
            {feedOverCreatePoll ? <ContentFeed /> : <CreatePollComponent />}
            <SideBarRight feedOverCreatePoll={feedOverCreatePoll} setFeedOverCreatePoll={setFeedOverCreatePoll} />
        </div>
    )
}

export default MainContent
