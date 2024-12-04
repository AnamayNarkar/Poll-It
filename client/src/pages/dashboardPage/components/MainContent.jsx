import React from 'react'
import '../styles/MainContentStyles.css'
import SideBar from './SideBar'

const MainContent = () => {
    return (
        <div className='mainContentContainer' style={{ display: 'flex', flexDirection: 'row', height: '90%', width: '100%', padding: '0', margin: '0' }}>
            <SideBar />
        </div>
    )
}

export default MainContent
