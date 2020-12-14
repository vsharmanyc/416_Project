import React, { Component } from 'react';
import { Table } from 'react-bootstrap';
import '../App.css';


class Data extends Component {

    constructor(props) {
        super(props);
        this.state = {
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    strNumWithCommas = (num) => {
        if(num === undefined)
            return "";
        return parseInt(num).toLocaleString();
    }


    render() {
        let geoData = this.props.geoData;
        if(!this.props.stateIsSelected)
            geoData =  {};

        return (
            <div style={{textAlign: 'start'}}>
                <h5>{geoData.COUNTY === undefined ? "" : "County: " + geoData.COUNTY}</h5>
                <h5>{geoData.DISTRICTID === undefined || geoData.DISTRICTID === 'null' ? "" : "District: " + geoData.DISTRICTID}</h5>
                <h5>{geoData.district === undefined || geoData.district === 'null' ? "" : "District: " + geoData.district}</h5>
                <h5>{geoData.PRECINCT === undefined ? "" : "Precinct: " + geoData.PRECINCT}</h5>
                <Table striped bordered hover size="sm">
                    <thead>
                        <tr>
                            <th>Demographic</th>
                            <th>Population</th>
                            <th>VAP</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>White</td>
                            <td>{this.strNumWithCommas(geoData.WTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.WVAP)}</td>
                        </tr>
                        <tr>
                            <td>Black</td>
                            <td>{this.strNumWithCommas(geoData.BTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.BVAP)}</td>
                        </tr>
                        <tr>
                            <td>Asian</td>
                            <td>{this.strNumWithCommas(geoData.ATOT)}</td>
                            <td>{this.strNumWithCommas(geoData.AVAP)}</td>
                        </tr>
                        <tr>
                            <td>Hispanic or Latino</td>
                            <td>{this.strNumWithCommas(geoData.HTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.HVAP)}</td>
                        </tr>
                        <tr>
                            <td>American Indian or Alaska Native</td>
                            <td>{this.strNumWithCommas(geoData.AIANTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.AIANVAP)}</td>
                        </tr>
                        <tr>
                            <td>Native Hawaiian or Other Pacific Islander</td>
                            <td>{this.strNumWithCommas(geoData.NHOPTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.NHOPVAP)}</td>
                        </tr>
                        <tr>
                            <td>Other</td>
                            <td>{this.strNumWithCommas(geoData.OTHERTOT)}</td>
                            <td>{this.strNumWithCommas(geoData.OTHERVAP)}</td>
                        </tr>
                        <tr>
                            <td>Total</td>
                            <td>{this.strNumWithCommas(geoData.TOTAL)}</td>
                            <td>{this.strNumWithCommas(geoData.TOTVAP)}</td>
                        </tr>
                        <tr>
                            <td>Total Minority</td>
                            <td>{this.strNumWithCommas(geoData.TOTAL !== undefined ?  geoData.TOTAL - geoData.WTOT : undefined)}</td>
                            <td>{this.strNumWithCommas(geoData.TOTVAP !== undefined ? geoData.TOTVAP - geoData.WVAP : undefined)}</td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}

export default Data;
