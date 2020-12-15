import React, { Component } from 'react';
import CanvasJSReact from '../canvasjs-3.2.4/canvasjs.react';

var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;



class GraphModal extends Component {

    itemclick = (e) => {
        console.log(e);
        e.dataSeries.visible = !e.dataSeries.visible;            
        e.chart.render();
    }

    enumToDemographic = (demoEnum) => {
        switch (demoEnum) {
            case 'WHITE':
                return 'White';
            case 'AFRICAN_AMERICAN':
                return 'Black';
            case 'ASIAN':
                return 'Asian';
            case 'HISPANIC_LATINO':
                return 'Hispanic or Latino';
            case 'AM_INDIAN_AK_NATIVE':
                return 'American Indian or Alaska Native';
            case 'NH_OR_OPI':
                return 'Native Hawaiian or Other Pacific Islander';
            default:
                return 'Other'
        }
    }

    constructor(props) {
        super(props);
        this.enactedPlans = {
            NY: [
                { x: 0, label: "1", y: 0.04996367229503909 },
                { x: 1, label: "2", y: 0.051417164582353375 },
                { x: 2, label: "3", y: 0.05235506605797916 },
                { x: 3, label: "4", y: 0.059331663696029734 },
                { x: 4, label: "5", y: 0.06389096644435746 },
                { x: 5, label: "6", y: 0.10046321180314405 },
                { x: 6, label: "7", y: 0.10504840309577931 },
                { x: 7, label: "8", y: 0.11948568345754991 },
                { x: 8, label: "9", y: 0.12720125140465693 },
                { x: 9, label: "10", y: 0.1568856960585804 },
                { x: 10, label: "11", y: 0.1622881737378747 },
                { x: 11, label: "12", y: 0.1766785812386606 },
                { x: 12, label: "13", y: 0.17856945247376083 },
                { x: 13, label: "14", y: 0.18299055196605354 },
                { x: 14, label: "15", y: 0.2062851146913892 },
                { x: 15, label: "16", y: 0.2063579890313529 },
                { x: 16, label: "17", y: 0.28615094304370975 },
                { x: 17, label: "18", y: 0.28951547446801 },
                { x: 18, label: "19", y: 0.30685006234413964 },
                { x: 19, label: "20", y: 0.32903835398200093 },
                { x: 20, label: "21", y: 0.3716670805103284 },
                { x: 21, label: "22", y: 0.48981929867535945 },
                { x: 22, label: "23", y: 0.5520750144029243 },
                { x: 23, label: "24", y: 0.588101657679249 },
                { x: 24, label: "25", y: 0.6596212183323769 },
                { x: 25, label: "26", y: 0.6905133835618943 },
                { x: 26, label: "27", y: 0.7073342106591202 },
                { x: 27, label: "28", y: 0.7679416522496558 },
                { x: 28, label: "29", y: 0.9420703611648732 }
            ],
            MD: [
                { x: 0, label: "1", y: 0.06540697158947842 },
                { x: 1, label: "2", y: 0.11233730618214743 },
                { x: 2, label: "3", y: 0.16391968780613606 },
                { x: 3, label: "4", y: 0.1901751650495369 },
                { x: 4, label: "5", y: 0.31135247686496165 },
                { x: 5, label: "6", y: 0.3601981608689068 },
                { x: 6, label: "7", y: 0.5504211717002624 },
                { x: 7, label: "8", y: 0.5601521279761627 }
            ]
        }
    }

    render() {
        let job = this.props.job;

        if(job.enactedPlanData !== '')
            this.enactedPlans[job.state] = job.enactedPlanData.substring(1, job.enactedPlanData.length - 1).split(',').map((point, i) => ({ x: i, label: '' + (i + 1), y: parseFloat(point) }));

        let str = job.boxPlotData.substring(1, job.boxPlotData.length - 1);
        let dataPoints = str.split(',').map((points) => {
            let stats = points.split('-').map((stat) => parseFloat(stat))
            return { label: "" + stats[0], y: [stats[1], stats[2], stats[3], stats[4], stats[5]] };
        });

        let demograhics = "VAP (Voting Age Population) for:";
        job.demographicGroups.forEach(demoEnum => {
            demograhics += ' ' + this.enumToDemographic(demoEnum) + ','
        });
        demograhics = demograhics.substring(0, demograhics.length - 1) + ' demographic(s)';

        

        const options = {
            animationEnabled: true,
            theme: "light2",
            title: {
                text: "VAP% By District in " + job.state
            },
            axisX: {
                title: "District Number"
            },
            axisY: {
                title: "VAP%"
            },
            legend: {
                cursor: "pointer",
                itemclick: this.itemclick
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
                visible: true,
                markerType: "cross",
                markerColor: "red",
                name: "Enacted Plan",
                toolTipContent: `<span style="color:#C0504E">{name}</span>: {y}`,
                showInLegend: true,
                dataPoints: this.enactedPlans[job.state]
            },
            {
                type: "scatter",
                visible: false,
                markerType: "cross",
                markerColor: "blue",
                name: "Average Plan",
                toolTipContent: `<span style="color:#C0504E">{name}</span>: {y}`,
                showInLegend: true,
                dataPoints: job.averagePlanData.substring(1, job.averagePlanData.length - 1).split(',').map((point, i) => ({ x: i, label: '' + (i + 1), y: parseFloat(point) })),

            },
            {
                type: "scatter",
                visible: false,
                markerType: "cross",
                markerColor: "green",
                name: "Extreme Plan",
                toolTipContent: `<span style="color:#C0504E">{name}</span>: {y}`,
                showInLegend: true,
                dataPoints: this.props.job.extremePlanData.substring(1, job.extremePlanData.length - 1).split(',').map((point, i) => ({ x: i, label: '' + (i + 1), y: parseFloat(point) })),
            },
            {
                type: "scatter",
                visible: false,
                markerType: "cross",
                markerColor: "orange",
                name: "Random Plan",
                toolTipContent: `<span style="color:#C0504E">{name}</span>: {y}`,
                showInLegend: true,
                dataPoints: this.props.job.randomPlanData.substring(1, job.randomPlanData.length - 1).split(',').map((point, i) => ({ x: i, label: '' + (i + 1), y: parseFloat(point) })),
            },
            ],
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
                    <div style={{
                        textAlign: 'start',
                        fontSize: '100%',
                        fontFamily: 'monospace',
                        marginTop: '2%'
                    }}>
                        {demograhics}
                    </div>
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