import React from 'react';

const SearchedUpTagComponent = ({ tag, addTags }) => {
    return (
        tag.name !== '' ? (
            <div
                className="searchedUpTagComponentContainer"
                style={{
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
                onClick={() => addTags(tag)}
            >
                <h3
                    style={{
                        margin: '0px',
                        padding: '0px',
                        fontFamily: 'Lato, sans-serif',
                    }}
                >
                    {tag.name}
                </h3>
            </div>
        ) : null
    );
};

export default SearchedUpTagComponent;
