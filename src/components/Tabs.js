import React, { Component } from 'react';

class Tabs extends Component {
    constructor(props) {
        super(props);

        this.state = {
            tabsInnerJSX: Array.isArray(props.tabsInnerJSX) ? props.tabsInnerJSX : [],
            tabsNames: Array.isArray(props.tabsNames) ? props.tabsNames : [],
            currentTab: 0,
        };
    }

    tabClicked = (tabIndex) =>{
        this.setState({currentTab: tabIndex});
    }

    render() {

        return (
            <div style={{
                height: '100%',
                width: '100%',
            }}>
                <div style={{
                    height: '4%',
                    width: '100%',
                    display: 'flex',
                }}>
                    {this.state.tabsNames.map((tabName, tabIndex) => {
                        return (
                            <div style={{
                                width: (90 / this.state.tabsNames.length) + '%',
                                height: tabIndex === this.state.currentTab ? '100%' : '95%',
                                borderBottomColor: tabIndex === this.state.currentTab ? 'white' : 'black',
                                borderStyle: 'solid',
                                borderRadius: 5,
                                backgroundColor: 'white'
                            }}
                            onClick = {() => { this.tabClicked(tabIndex) }}
                            >
                                {tabName}
                            </div>
                        );
                    })}
                </div>

                <div style={{
                    width: '99%',
                    height: '95.5%',
                    borderStyle: 'solid',
                    borderRadius: 5,
                    backgroundColor: 'white'
                }}>
                    {this.state.tabsInnerJSX[this.state.currentTab]}
                </div>
            </div>
        );
    }

}

export default Tabs;