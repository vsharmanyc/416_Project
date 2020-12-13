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
                <h5>{this.props.geoData.COUNTY === undefined ? "" : "County: " + this.props.geoData.COUNTY}</h5>
                <h5>{this.props.geoData.DISTRICTID === undefined || this.props.geoData.DISTRICTID === 'null' ? "" : "District: " + this.props.geoData.DISTRICTID}</h5>
                <h5>{this.props.geoData.district === undefined || this.props.geoData.district === 'null' ? "" : "District: " + this.props.geoData.district}</h5>
                <h5>{this.props.geoData.PRECINCT === undefined ? "" : "Precinct: " + this.props.geoData.PRECINCT}</h5>
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
                            <td>{this.strNumWithCommas(this.props.geoData.WTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.WVAP)}</td>
                        </tr>
                        <tr>
                            <td>Black</td>
                            <td>{this.strNumWithCommas(this.props.geoData.BTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.BVAP)}</td>
                        </tr>
                        <tr>
                            <td>Asian</td>
                            <td>{this.strNumWithCommas(this.props.geoData.ATOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.AVAP)}</td>
                        </tr>
                        <tr>
                            <td>Hispanic or Latino</td>
                            <td>{this.strNumWithCommas(this.props.geoData.HTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.HVAP)}</td>
                        </tr>
                        <tr>
                            <td>American Indian or Alaska Native</td>
                            <td>{this.strNumWithCommas(this.props.geoData.AIANTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.AIANVAP)}</td>
                        </tr>
                        <tr>
                            <td>Native Hawaiian or Other Pacific Islander</td>
                            <td>{this.strNumWithCommas(this.props.geoData.NHOPTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.NHOPVAP)}</td>
                        </tr>
                        <tr>
                            <td>Other</td>
                            <td>{this.strNumWithCommas(this.props.geoData.OTHERTOT)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.OTHERVAP)}</td>
                        </tr>
                        <tr>
                            <td>Total</td>
                            <td>{this.strNumWithCommas(this.props.geoData.TOTAL)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.TOTVAP)}</td>
                        </tr>
                        <tr>
                            <td>Total Minority</td>
                            <td>{this.strNumWithCommas(this.props.geoData.TOTAL !== undefined ?  this.props.geoData.TOTAL - this.props.geoData.WTOT : undefined)}</td>
                            <td>{this.strNumWithCommas(this.props.geoData.TOTVAP !== undefined ? this.props.geoData.TOTVAP - this.props.geoData.WVAP : undefined)}</td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}

export default Data;
