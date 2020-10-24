import React, { Component } from 'react';
import { Button, Link } from '@material-ui/core/';


class SettingsTab extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showSources: false
        }

    }

    render() {
        const sources = [
            { name: 'Maryland Source', url: 'https://github.com/mggg-states/MD-shapefiles' },
            { name: 'Pennsylvania Source', url: 'https://github.com/mggg-states/PA-shapefiles' },
            { name: 'New York Source', url: 'https://dataverse.harvard.edu/dataset.xhtml?persistentId=hdl:1902.1/16320&studyListingIndex=2_3cfc56a7c5a06219bd1114590f1c' }
        ];

        return (
            <div style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
            }}>
                <Button style={{ backgroundColor: "#63BEB6", marginTop: '2%' }}
                    onClick={() => { this.setState({ showSources: !this.state.showSources }) }}>
                    {this.state.showSources ? 'Hide Sources' : 'Show Sources'}
                </Button>
                {this.state.showSources ?
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center',
                        alignItems: 'center',
                        borderRadius: '5%',
                        marginTop: '2%',
                        padding: '2%',
                        width: '90%',
                        backgroundColor: '#ededed',
                    }}>
                        {sources.map((source) => {
                            return (<Link variant="body2" href={source.url} target="_blank">{source.name}</Link>)
                        })}<br />
                    </div>
                    : <></>
                }
            </div>
        );
    }

}

export default SettingsTab