import React from 'react'
import '../styles/TopSearchBarResultComponentStyles.css'

const TopSearchBarResultComponent = ({ result, typeOfSearch }) => {
    return (
        <div className='singularSearchResultContainer' >
            <div className='searchResultName' >
                {typeOfSearch === 'tag' ? "t/" + result.name : typeOfSearch === 'user' ? "u/" + result.username : result.name}
            </div>
        </div >
    )
}

export default TopSearchBarResultComponent
