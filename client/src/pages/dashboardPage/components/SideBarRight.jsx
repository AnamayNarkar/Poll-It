import React from 'react'
import '../styles/SideBarRightStyles.css'
import { logoutRequest } from '../../../services/ApiRequests/authRequests';

const SideBarRight = ({ feedOverCreatePoll, setFeedOverCreatePoll }) => {

    function setFeedOverCreatePollCustom() {
        setFeedOverCreatePoll(!feedOverCreatePoll);
    }

    return (
        <div className='sideBarRightContainer'>
            <div className='createPollContainer'>
                <div className='createPollButton' onClick={setFeedOverCreatePollCustom}>
                    <h4>Create Poll</h4>
                </div>
            </div>
            <div className='aboutAndLogoutContainer'>
                <div className='aboutFieldContainer'>
                    <div className='oneMoreContainerForHoverEffect'>
                        <h4>About</h4>
                    </div>
                </div>
                <div className='logoutFieldContainer'>
                    <div className='oneMoreContainerForHoverEffect' onClick={() => {
                        logoutRequest();
                    }}>
                        <h4>
                            Logout</h4>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default SideBarRight
