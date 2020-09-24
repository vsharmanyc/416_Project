import React, { Component } from 'react';
import { renderIntoDocument } from 'react-dom/test-utils';

class Sidebar extends Component {
    constructor(props) {
        super(props);
        let side = this.props.side === 'left' || this.props.side === 'right' ? this.props.side : 'left';

        this.state = {
            width: props.width,
            height: props.height,
            isExpanded: true,
            side: side,
            expandSymbol: side === 'left' ? '🡆' : '🡄',
            hideSymbol: side === 'left' ? '🡄' : '🡆'
        };
    }

    toggleSidebar = () => {
        this.setState({ isExpanded: !this.state.isExpanded });
    }

    render() {
        const borderColor = '#c9c9c9';
        const backgroundColor = '#ebebeb';

        return (
            <div style={{
                position: 'absolute',
                zIndex: 100,
                display: 'flex',
                flexDirection: this.state.side === 'left' ? 'row' : 'row-reverse',
                justifyContent: 'center',
                alignItems: 'center',
                right: this.state.side === 'right' ? 0 : null
            }}>

                {this.state.isExpanded ?
                    <div style={{
                        width: this.state.width,
                        height: this.state.height,
                        backgroundColor: backgroundColor,
                        borderColor: borderColor,
                        borderStyle: 'solid',
                        borderWidth: 3
                    }}>
                    </div>
                    :
                    <div></div>
                }


                <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    height: this.state.height,
                    width: this.state.width * .1
                }}>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        width: this.state.width * .1,
                        height: this.state.height * .35,
                        backgroundColor: backgroundColor,
                        borderRightColor: this.state.side === 'left' ? borderColor : backgroundColor,
                        borderTopColor: borderColor,
                        borderBottomColor: borderColor,
                        borderLeftColor: this.state.side === 'left' ? backgroundColor : borderColor,
                        borderStyle: 'solid',
                        borderWidth: 3
                    }}
                        onClick={this.toggleSidebar}
                    >
                        <div style={{
                            position: 'absolute',
                            fontSize: this.state.height * .045,
                        }}>
                            {this.state.isExpanded ? this.state.hideSymbol : this.state.expandSymbol}
                        </div>
                    </div>
                </div>

            </div >
        );
    }

}

export default Sidebar;