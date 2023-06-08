import React, {useEffect, useState} from 'react';
import {Box, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography} from '@mui/material';
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import axios from 'axios';
import {API_URL} from '../../consts';
import {BuildingFromList} from '../../types/buildingFromList';
import {Place} from '../../types/place';
import IconButton from "@mui/material/IconButton";
import IconViewList from "../icons/IconViewList";

const Building = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [building, setBuilding] = useState<BuildingFromList | null>(null);
    const buildingId = useParams().buildingId;
    const [places, setPlaces] = useState<Place[]>([]);
    const fetchData = async () => {
        axios.get(`${API_URL}/buildings/building/${buildingId}`, {
            headers: {
                'Authorization': token
            }
        }).then(response => {
            setBuilding(response.data);
        }).catch(error => {
            if (error.response.status == 500) navigate('/');
        })

        axios.get(`${API_URL}/buildings/building/${buildingId}/places`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setPlaces(response.data);
        }).catch(error => {
            if (error.response.status == 500) navigate('/');
        })
    };

    useEffect(() => {
        fetchData();
    }, [buildingId]);

    const goToPlace = (placeId: string) => {
        navigate('/places/place/' + placeId);
    }

    return (
        <div style={{height: '93.3vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <Box sx={{width: '50%', maxWidth: '600px', margin: '2vh'}}>
                {building !== null && (
                    <Paper elevation={3} style={{padding: '2vh'}}>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.created_by')}:</b> {building.createdBy}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.total_area')}:</b> {building.totalArea}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.communal_area_aggregate')}:</b> {building.communalAreaAggregate}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.street')}:</b> {building.street}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.building_number')}:</b> {building.buildingNumber}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.city')}:</b> {building.city}
                        </Typography>
                        <Typography variant="h5">
                            <b>{t('buildingFromList.postal_code')}:</b> {building.postalCode}
                        </Typography>
                    </Paper>
                )}
            </Box>

            <TableContainer component={Paper}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                {t('place.created_by')}
                            </TableCell>
                            <TableCell>
                                {t('place.placeNumber')}
                            </TableCell>
                            <TableCell>
                                {t('place.area')}
                            </TableCell>
                            <TableCell>
                                {t('place.hotWaterConnection')}
                            </TableCell>
                            <TableCell>
                                {t('place.centralHeatingConnection')}
                            </TableCell>
                            <TableCell>
                                {t('place.predictedHotWaterConsumption')}
                            </TableCell>
                            <TableCell>
                                {t('place.firstName')}
                            </TableCell>
                            <TableCell>
                                {t('place.surname')}
                            </TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {places.map((places) => (
                            <TableRow key={places.id}>
                                <TableCell>{places.createdBy}</TableCell>
                                <TableCell>{places.placeNumber}</TableCell>
                                <TableCell>{places.area}</TableCell>
                                <TableCell>{places.hotWaterConnection}</TableCell>
                                <TableCell>{places.centralHeatingConnection}</TableCell>
                                <TableCell>{places.predictedHotWaterConsumption}</TableCell>
                                <TableCell>{places.firstName}</TableCell>
                                <TableCell>{places.surname}</TableCell>
                                <TableCell>
                                    <IconButton
                                        edge="start"
                                        color="inherit"
                                        onClick={() => goToPlace(places.id.toString())}
                                        aria-label="close"
                                    >
                                        <IconViewList/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

        </div>
    );
}

export default Building;