import React from 'react'
import '../styles/SideBarStyles.css'


const SideBar = () => {

    const tagsYouFollowSampleData = [
        {
            name: 'React',
            id: 1
        },

        {
            name: 'Node.js',
            id: 2
        },

        {
            name: 'Express.js',
            id: 3
        },

        {
            name: 'MongoDB',
            id: 4
        },

        {
            name: 'JavaScript',
            id: 5
        },

        {
            name: 'Python',
            id: 6
        },

        {
            name: 'Java',
            id: 7
        },
        {
            name: 'C++',
            id: 8
        },
        {
            name: 'C#',
            id: 9
        }, {
            name: 'Ruby',
            id: 10
        }, {
            name: 'Ruby on Rails',
            id: 11
        }, {
            name: 'Django',
            id: 12
        }, {
            name: 'Flask',
            id: 13
        }
    ]

    return (
        <div className='sideBarContainer'>
            <div className='homeAndPopular'>
                <div className='homeOption'>
                    <h4>Home</h4>
                </div>
                <div className='popularOption'>
                    <h4>Popular</h4>
                </div>
            </div>
            <div className='tagsYouFollow'>
                <div className='tagsYouFollowHeader'>
                    <h4>Tags you follow</h4>
                </div>
                <div className='tagsYouFollowList'>
                    {
                        tagsYouFollowSampleData.map((tag) => {
                            return (
                                <div key={tag.id} className='tagYouFollowIndividual'>
                                    <h5>{tag.name}</h5>
                                </div>
                            )
                        })
                    }
                </div>
            </div>
            <div className='aboutFieldContainer'>
                <div className='oneMoreContainerForHoverEffect'>
                    <h4>About</h4>
                </div>
            </div>
        </div>
    )
}
import '../styles/SideBarStyles.css'

export default SideBar
