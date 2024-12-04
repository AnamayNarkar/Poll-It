import React from 'react'


const SearchedUpTagComponent = ({ tag, addedTags, setAddedTags }) => {
    return (
        <div className='searchedUpTagComponentContainer' style={{

            backgroundColor: 'azure',
            height: '50px',
            width: 'auto',
            paddingLeft: '10px',
            paddingRight: '10px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            borderRadius: '10px',
            cursor: 'pointer',

        }}
            onClick={() => {
                if (addedTags.find((addedTag) => addedTag.id === tag.id)) {
                    window.alert('Tag already added');
                }
                else if (addedTags.length >= 3) {
                    window.alert('Maximum 3 tags allowed');
                } else {
                    setAddedTags([...addedTags, tag]);
                }
            }}
        >
            <h3 style={{
                margin: '0px',
                padding: '0px',
                fontFamily: 'Lato , sans-serif'
            }}>
                {tag.name}
            </h3>
        </div>
    )
}

export default SearchedUpTagComponent
