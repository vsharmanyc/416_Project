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


    render() {

        return (
            <div>
                <h5>{this.props.geoData.COUNTY}</h5>
                <h5>{this.props.geoData.PRECINCTNAME}</h5>
                <Table striped bordered hover size="sm">
                    <thead>
                        <tr>
                            <th>Demographic</th>
                            <th>Population</th>
                            <th>Voting Age Population</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>White</td>
                            <td>{this.props.geoData.WTOT}</td>
                            <td>{this.props.geoData.WVAP}</td>
                        </tr>
                        <tr>
                            <td>Black</td>
                            <td>{this.props.geoData.BTOT}</td>
                            <td>{this.props.geoData.BVAP}</td>
                        </tr>
                        <tr>
                            <td>Asian</td>
                            <td>{this.props.geoData.ATOT}</td>
                            <td>{this.props.geoData.AVAP}</td>
                        </tr>
                        <tr>
                            <td>Hispanic and Latino</td>
                            <td>{this.props.geoData.HLTOT}</td>
                            <td>{this.props.geoData.HLVAP}</td>
                        </tr>
                        <tr>
                            <td>American Indian and Alaska Native</td>
                            <td>{this.props.geoData.AIANTOT}</td>
                            <td>{this.props.geoData.AIANVAP}</td>
                        </tr>
                        <tr>
                            <td>Two or More Races</td>
                            <td>{this.props.geoData.TWOMORETOT}</td>
                            <td>{this.props.geoData.TWOMOREVAP}</td>
                        </tr>
                        <tr>
                            <td>Other</td>
                            <td>{this.props.geoData.OTHERTOT}</td>
                            <td>{this.props.geoData.OTHERVAP}</td>
                        </tr>
                        <tr>
                            <td>Total</td>
                            <td>{this.props.geoData.TOTAL}</td>
                            <td>{this.props.geoData.TOTVAP}</td>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}

export default Data;
