import React, { Component } from 'react';
import CanvasJSReact from '../canvasjs-3.2.4/canvasjs.react';

var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;



class GraphModal extends Component {
    render() {
        const options = {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "Life Expectancy - Europe"
            },
            axisY: {
                title: "Age (in years)"
            },
            data: [{
                type: "boxAndWhisker",
                color: "black",
                upperBoxColor: "#A3A3A3",
                lowerBoxColor: "#494949",
                yValueFormatString: "###.0# years",
                dataPoints: [
                    { label: "Germany", y: [67.5, 70.55, 76.705, 79.406, 73.15] },
                    { label: "France", y: [67.41, 71.03, 78.05, 80.657, 74.36] },
                    { label: "Spain", y: [64.94, 70.565, 78.17, 80.94, 75.345] },
                    { label: "UK", y: [69.18, 71.06, 76.819, 79.425, 73.4] },
                    { label: "Switzerland", y: [69.62, 72.045, 78.7, 81.70, 75.8] },
                    { label: "Greece", y: [65.86, 70.255, 78.0625, 85, 75.24] },
                    { label: "Poland", y: [61.31, 68.625, 72.035, 75.56, 70.915] }
                ]
            }]
        }
        return (
            <div style={{
                display: 'flex',
                justifyContent: 'center',
                alignContent: 'center',
                alignItems: 'center',
                height: '70%',
                width: '70%',
                backgroundColor: 'white',
                border: 'solid',
                borderWidth: '3px',
                borderColor: '#63BEB6',
                position: 'relative'
            }}>
                <div style={{ height: '90%', width: '90%' }}>
                    <CanvasJSChart options={options} />
                </div>
                <button style={{ position: 'absolute', top: 0, right: 0, color: '#FFFFFF', fontSize: '150%', backgroundColor: '#c0c0c0' }}
                    onClick={this.props.toggleModal}>
                    ✖
                </button>
            </div>
        );
    }
}

export default GraphModal;                    