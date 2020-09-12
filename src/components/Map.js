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
                zoom={5}
                style={mapStyles}
                initialCenter={
                    {
                        lat: 38.9618303,
                        lng: -96.6980505
                    }
                }
            />
        );
    }
}

export default GoogleApiWrapper({
    apiKey: 'AIzaSyCphiTQeeYRd23P6L8DTK9ie8Qcy2tSya4'
})(MapContainer);