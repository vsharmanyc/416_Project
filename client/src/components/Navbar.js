import React, { Component } from 'react';
import panther from "./../rsrc/panther.png"

class Navbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            width: window.innerWidth,
            height: window.innerHeight,
        };
    }

    componentDidMount() {
        window.addEventListener('resize', this.updateWindowDimensions);
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.updateWindowDimensions);
    }

    updateWindowDimensions = () => {
        this.setState({ width: window.innerWidth, height: window.innerHeight }, this.forceUpdate);
    }

    render() {
        let sections = [];
        if (this.props.sections != null && Array.isArray(this.props.sections))
            sections = this.props.sections;

        return (
            <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                width: this.state.width,
                height: this.state.height * 0.10,
            }}>
                <div style={{ position: 'absolute', zIndex: 99 }}>
                    <img style={{
                        marginTop: '3%',
                        height: this.state.height * .12,
                        width: this.state.height * .12,
                    }} src={panther} />
                </div>

                <div style={{
                    width: '99%',
                    height: '80%',
                    backgroundColor: '#63BEB6',
                }}>
                    <div style={{
                        position: 'absolute',
                        right: 0,
                        width: (7.5 * sections.length) + '%',
                        display: 'flex',
                        justifyContent: 'space-around'
                    }}>
                        {sections.map((section, index) => {
                            return (<h3>{section}</h3>);
                        })}
                    </div>
                </div>
            </div>
        );
    }
}

export default Navbar;