import React from 'react'

import DashboardHeader from './components/DashboardHeader'
import MainContent from './components/MainContent'

const DashboardPage = () => {
    return (
        <div className='dashboardPageContainer' style={{ backgroundColor: '#242424', height: '100%', width: '100%', margin: '0', padding: '0', display: 'flex', flexDirection: 'column' }}>
            <DashboardHeader />
            <MainContent />
        </div >
    )
}

export default DashboardPage
