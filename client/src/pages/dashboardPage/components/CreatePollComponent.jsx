import React, { useEffect, useRef } from 'react';
import '../styles/CreatePollComponentStyles.css';
import OptionsFieldInCreatePollComponent from './OptionsFieldInCreatePollComponent';
import AddedTagComponent from './AddedTagComponent';
import SearchAndCreateTagsComponent from './SearchAndCreateTagsComponent';

const CreatePollComponent = () => {
    const [numberOfOptions, setNumberOfOptions] = React.useState(2);

    const [addedTags, setAddedTags] = React.useState([
        { name: 'tag1tatatatat', id: 1 },
        { name: 'tag2', id: 2 },
        { name: 'tag3', id: 3 }
    ]);

    const [isSearchTagsComponentVisible, setIsSearchTagsComponentVisible] = React.useState(false);

    const searchTagsRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (
                searchTagsRef.current &&
                !searchTagsRef.current.contains(event.target)
            ) {
                setIsSearchTagsComponentVisible(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <div className='createPollComponentContainer'>
            {isSearchTagsComponentVisible && (
                <div ref={searchTagsRef}>
                    <SearchAndCreateTagsComponent />
                </div>
            )}
            <div className='createPollComponentBody'>
                <div className='createPollComponentBodyQuestion'>
                    <h3>Question</h3>
                    <input type='text' />
                </div>

                <div className='howManyOptionsDropdown'>
                    <h3>How many options?</h3>
                    <select
                        onChange={(e) => setNumberOfOptions(Number(e.target.value))}
                        value={numberOfOptions}
                    >
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                    </select>
                </div>

                <h3 className='OptionsTheWord'>Options</h3>

                <div className='optionsFieldArrayParent'>
                    {Array.from({ length: numberOfOptions }).map((_, i) => (
                        <OptionsFieldInCreatePollComponent key={i} />
                    ))}
                </div>

                <div className='createPollComponentBodyAddTags'>
                    <div className='createPollComponentBodyAddTagsHeader'>
                        <h3>Add Tags</h3>
                        <img
                            src='https://github.com/user-attachments/assets/633813f5-1679-4b96-bfca-b8f7c37c1693'
                            alt='plus'
                            onClick={() =>
                                setIsSearchTagsComponentVisible(!isSearchTagsComponentVisible)
                            }
                        />
                    </div>
                    <div className='addedTags'>
                        {addedTags.map((tag) => (
                            <AddedTagComponent
                                key={tag.id}
                                tag={tag}
                                allTagsArray={addedTags}
                                setAllTagsArray={setAddedTags}
                            />
                        ))}
                    </div>
                </div>

                <div className='createPollComponentBodySubmitButtonContainer'>
                    <button>Submit</button>
                </div>
            </div>
        </div>
    );
};

export default CreatePollComponent;
