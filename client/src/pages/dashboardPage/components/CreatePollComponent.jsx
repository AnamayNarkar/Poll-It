import React, { useEffect, useRef } from 'react';
import '../styles/CreatePollComponentStyles.css';
import OptionsFieldInCreatePollComponent from './OptionsFieldInCreatePollComponent';
import AddedTagComponent from './AddedTagComponent';
import SearchAndCreateTagsComponent from './SearchAndCreateTagsComponent';
import axios from 'axios';

const CreatePollComponent = () => {
    const [numberOfOptions, setNumberOfOptions] = React.useState(2);
    const [isSearchTagsComponentVisible, setIsSearchTagsComponentVisible] = React.useState(false);
    const searchTagsRef = useRef(null);

    const [createPollFormState, setCreatePollFormState] = React.useState({
        question: '',
        options: Array(2).fill(''),
        tags: [],
        expirationDateTime: '',
    });

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

    const handleOptionChange = (index, value) => {
        const updatedOptions = [...createPollFormState.options];
        updatedOptions[index] = value;
        setCreatePollFormState((prevState) => ({
            ...prevState,
            options: updatedOptions,
        }));
        console.log(createPollFormState);
    };

    const handleNumberOfOptionsChange = (value) => {
        const newNumberOfOptions = Number(value);
        setNumberOfOptions(newNumberOfOptions);
        setCreatePollFormState((prevState) => ({
            ...prevState,
            options: Array(newNumberOfOptions)
                .fill('')
                .map((_, i) => prevState.options[i] || ''),
        }));
    };
    async function handleCreatePollFormSubmit() {
        console.log(createPollFormState);

        // Example axios call
        const response = await axios.post(
            'http://localhost:3000/api/poll/createPoll',
            createPollFormState,
            { withCredentials: true }
        );
        console.log(response.data);
    }


    function addTags(tag) {

        if (createPollFormState.tags.find((addedTag) => addedTag.id === tag.id)) {
            window.alert('Tag already added');
        } else if (createPollFormState.tags.length >= 3) {
            window.alert('Maximum 3 tags allowed');
        } else {
            setCreatePollFormState((prevState) => ({
                ...prevState,
                tags: [...prevState.tags, tag],
            }));
        }

    }

    function removeTag(tag) {
        setCreatePollFormState({
            ...createPollFormState,
            tags: createPollFormState.tags.filter((addedTag) => addedTag.id !== tag.id),
        });
    }

    return (
        <div className='createPollComponentContainer'>
            {isSearchTagsComponentVisible && (
                <div ref={searchTagsRef}>
                    <SearchAndCreateTagsComponent addTags={addTags} />
                </div>
            )}
            <div className='createPollComponentBody'>
                <div className='createPollComponentBodyQuestion'>
                    <h3>Question</h3>
                    <input
                        type='text'
                        onChange={(e) =>
                            setCreatePollFormState({
                                ...createPollFormState,
                                question: e.target.value,
                            })
                        }
                        value={createPollFormState.question}
                    />
                </div>

                <div className='howManyOptionsDropdown'>
                    <h3>How many options?</h3>
                    <select
                        onChange={(e) => handleNumberOfOptionsChange(e.target.value)}
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
                    {createPollFormState.options.map((option, i) => (
                        <OptionsFieldInCreatePollComponent
                            key={i}
                            id={i}
                            value={option}
                            onChange={(value) => handleOptionChange(i, value)}
                        />
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
                        {createPollFormState.tags.map((tag) => (
                            <AddedTagComponent
                                key={tag.id}
                                tag={tag}
                                removeTag={removeTag}
                            />
                        ))}
                    </div>
                </div>

                <div className='createPollComponentBodyExpirationDate'>
                    <h3>Expiration Date (optional)</h3>
                    <input
                        type='date'
                        onChange={(e) =>
                            createPollFormState.expirationDateTime = `${e.target.value}T23:59:59.999Z`
                        }
                        value={createPollFormState.expirationDateTime.split('T')[0]}
                    />
                </div>

                <div className='createPollComponentBodySubmitButtonContainer'>
                    <button
                        onClick={handleCreatePollFormSubmit}
                        className='createPollComponentBodySubmitButton'
                    >
                        Submit
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CreatePollComponent;