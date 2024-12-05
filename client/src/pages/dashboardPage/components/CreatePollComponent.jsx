import React, { useEffect, useRef } from 'react';
import '../styles/CreatePollComponentStyles.css';
import OptionsFieldInCreatePollComponent from './OptionsFieldInCreatePollComponent';
import AddedTagComponent from './AddedTagComponent';
import SearchAndCreateTagsComponent from './SearchAndCreateTagsComponent';
import axios from 'axios';
import createPollRequest from '../../../services/ApiRequests/createPollRequest';

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

    function validateFormStateBeforeSubmit() {
        if (createPollFormState.question.trim().length === 0) {
            window.alert('Please provide a question.');
            return false;
        }

        if (createPollFormState.question.trim().length > 200) {
            window.alert('Question is too long. Maximum 200 characters allowed.');
            return false;
        }

        if (createPollFormState.options.some((option) => option.trim().length === 0)) {
            window.alert('Please fill in all options.');
            return false;
        }

        if (createPollFormState.tags.length === 0) {
            window.alert('Please add at least one tag.');
            return false;
        }

        if (createPollFormState.expirationDateTime) {
            const expirationDate = new Date(createPollFormState.expirationDateTime);
            const minAllowedDate = new Date(Date.now() + 2 * 24 * 60 * 60 * 1000);
            if (expirationDate < minAllowedDate) {
                window.alert('Expiration date must be at least 2 days from now.');
                return false;
            }
        }

        return true;
    }


    async function handleCreatePollFormSubmit() {

        if (!validateFormStateBeforeSubmit()) {
            return;
        }

        createPollFormState.question = createPollFormState.question.trim();
        createPollFormState.options = createPollFormState.options.map((option) => option.trim());
        createPollFormState.expirationDateTime = createPollFormState.expirationDateTime.trim();

        const response = await createPollRequest(createPollFormState);

        window.alert(response.message);

        setCreatePollFormState({
            question: '',
            options: Array(numberOfOptions).fill(''),
            tags: [],
            expirationDateTime: '',
        });

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