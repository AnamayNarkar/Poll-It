import React, { useState } from 'react'
import '../styles/SearchAndCreateTagsComponent.css'
import SearchForTagsContainer from './SearchForTagsContainer';
import CreateTagsContainer from './CreateTagsContainer';

const SearchAndCreateTagsComponent = ({ addedTags, setAddedTags }) => {

    const [isSearchOverCreate, setIsSearchOverCreate] = useState(true);

    return (
        <div className='searchAndCreateTagsComponentContainer'>
            <div className='searchAndCreateTagsComponentSearchContainerHeader'>

                <h3 className='searchAndCreateTagsComponentSearchHeaderTitle' onClick={() => setIsSearchOverCreate(true)} style={{
                    backgroundColor: isSearchOverCreate ? 'rgb(54, 50, 50)' : 'rgb(45, 42, 42)'
                }}>
                    Search Tags
                </h3>
                <h3 className='searchAndCreateTagsComponentCreateHeaderTitle' onClick={() => setIsSearchOverCreate(false)} style={{
                    backgroundColor: isSearchOverCreate ? 'rgb(45, 42, 42)' : 'rgb(54, 50, 50)'
                }}>
                    Create Tags
                </h3>
            </div>

            <div className='searchAndCreateTagsComponentMain'>
                {isSearchOverCreate ? <SearchForTagsContainer addedTags={addedTags} setAddedTags={setAddedTags} />
                    : <CreateTagsContainer />}
            </div>

        </div >
    )
}

export default SearchAndCreateTagsComponent
