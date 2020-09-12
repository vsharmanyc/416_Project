import React, { Component } from 'react';
import { Map, GoogleApiWrapper } from 'google-maps-react';

const mapStyles = {

    width: '100%',
    height: '100%'
};

export class MapContainer extends Component {
    render() {
        return (
            <Map
                google={this.props.google}
                zoom={14}
                style={mapStyles}
                initialCenter={
                    {
                        lat: 38.1561929,
                        lng: -98.1595351
                    }
                }
            />
        );
    }
}

export default GoogleApiWrapper({
    apiKey: 'AIzaSyCphiTQeeYRd23P6L8DTK9ie8Qcy2tSya4'
})(MapContainer);