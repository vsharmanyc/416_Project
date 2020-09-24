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
        console.log(this.state);
        return (
            <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                width: this.state.width,
                height: this.state.height * 0.10,
            }}>
                <div style={{position: 'absolute', zIndex: 99 }}>
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
                        width: '15%',
                        display: 'flex',
                        marginLeft: '85%',
                        justifyContent: 'space-around'
                    }}>
                        <h3>Graphs</h3>
                        <h3>Sources</h3>
                    </div>
                </div>
            </div>
        );
    }
}

export default Navbar;