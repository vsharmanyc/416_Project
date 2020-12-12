import React, { Component } from 'react';
import CanvasJSReact from '../canvasjs-3.2.4/canvasjs.react';

var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;



class GraphModal extends Component {
    render() {
        let str = "[1-0.0644-0.0951-0.1238-0.1993-0.1103, 2-0.0935-0.1324-0.1782-0.2336-0.1513, 3-0.1303-0.1709-0.2147-0.2793-0.1931, 4-0.1672-0.209-0.26-0.3146-0.2328, 5-0.2077-0.2627-0.3144-0.3708-0.2874, 6-0.2606-0.3221-0.3824-0.4544-0.3579, 7-0.303-0.3845-0.4483-0.5187-0.4174, 8-0.3941-0.4822-0.5387-0.6437-0.5159]";
        
        str = str.substring(1, str.length - 1);
        let dataPoints = str.split(',').map((points) => {
            let stats = points.split('-').map((stat) => parseFloat(stat))
            return { label: "District " + stats[0], y: [stats[1], stats[2], stats[3], stats[4], stats[5]] };
        });

        
        const options = {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "Data"
            },
            axisX: {
                title: "District Number"
            },
            axisY: {
                title: "Quartiles"
            },
            data: [{
                type: "boxAndWhisker",
                color: "black",
                upperBoxColor: "#A3A3A3",
                lowerBoxColor: "#494949",
                yValueFormatString: "0.###",
                dataPoints: dataPoints
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