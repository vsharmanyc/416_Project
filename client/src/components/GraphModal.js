import React, { Component } from 'react';
import CanvasJSReact from '../canvasjs-3.2.4/canvasjs.react';

var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;



class GraphModal extends Component {
    render() {
         
        let str = this.props.job.boxPlotData.substring(1, this.props.job.boxPlotData.length - 1);
        let dataPoints = str.split(',').map((points) => {
            let stats = points.split('-').map((stat) => parseFloat(stat))
            return { label: "" + stats[0], y: [stats[1], stats[2], stats[3], stats[4], stats[5]] };
        });

        
        const options = {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "BVAP% By District in " + this.props.job.state
            },
            axisX: {
                title: "District Number"
            },
            axisY: {
                title: "BVAP%"
            },
            data: [{
                type: "boxAndWhisker",
                color: "black",
                upperBoxColor: "#A3A3A3",
                lowerBoxColor: "#494949",
                yValueFormatString: "0.###",
                dataPoints: dataPoints
            }, 
            {
                type: "scatter",
                name: "Enacted Plan",
                toolTipContent: `<span style="color:#C0504E">{name}</span>: {y} km/s`,
                showInLegend: true,
                dataPoints: [
                    { x: 0, label: "1", y: 0.06540697158947842 },
                    { x: 1, label: "2", y: 0.11233730618214743 },
                    { x: 2, label: "3", y: 0.16391968780613606 },
                    { x: 3, label: "4", y: 0.1901751650495369 },
                    { x: 4, label: "5", y: 0.31135247686496165 },
                    { x: 5, label: "6", y: 0.3601981608689068 },
                    { x: 6, label: "7", y: 0.5504211717002624 },
                    { x: 7, label: "8", y: 0.5601521279761627 }
                ]
            }],
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