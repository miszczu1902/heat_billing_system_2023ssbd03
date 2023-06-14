import React, {useEffect, useState} from 'react';
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import {API_URL} from '../../consts';
import {Place} from '../../types/placesList';
import IconButton from "@mui/material/IconButton";
import IconViewList from "../icons/IconViewList";

const PlacesList = () => {
    const {t, i18n} = useTranslation();
    const navigate = useNavigate();
    const token = 'Bearer ' + localStorage.getItem("token");
    const [places, setPlaces] = useState<Place[]>([]);
    const fetchData = async () => {
        axios.get(`${API_URL}/places/self`, {
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
    }, [token]);

    const goToPlace = (placeId: string) => {
        navigate('/places/place/' + placeId);
    }

    return (
        <div style={{height: '90vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <TableContainer component={Paper}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
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
                                {t('buildingFromList.building_number')}
                            </TableCell>
                            <TableCell>
                                {t('buildingFromList.city')}
                            </TableCell>
                            <TableCell>
                                {t('buildingFromList.postal_code')}
                            </TableCell>
                            <TableCell>
                                {t('buildingFromList.street')}
                            </TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {places.map((places) => (
                            <TableRow key={places.id}>
                                <TableCell>{places.placeNumber}</TableCell>
                                <TableCell>{places.area}</TableCell>
                                <TableCell>{places.hotWaterConnection ? t('place.connected') : t('place.disconnected')}</TableCell>
                                <TableCell>{places.centralHeatingConnection ? t('place.connected') : t('place.disconnected')}</TableCell>
                                <TableCell>{places.predictedHotWaterConsumption}</TableCell>
                                <TableCell>{places.buildingNumber}</TableCell>
                                <TableCell>{places.city}</TableCell>
                                <TableCell>{places.postalCode}</TableCell>
                                <TableCell>{places.street}</TableCell>
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

export default PlacesList;