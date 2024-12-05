import React from 'react'
import '../styles/TopSearchBarResultComponentStyles.css'

const TopSearchBarResultComponent = ({ result }) => {
    return (
        <div className='singularSearchResultContainer' >
            <div className='searchResultName' >
                {result.name}
            </div>
        </div >
    )
}

export default TopSearchBarResultComponent
