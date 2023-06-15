import validator from "validator";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { ButtonGroup, Button, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions, Snackbar, SnackbarContent, Switch, FormGroup, FormControlLabel} from '@mui/material';
import React, { useEffect, useState } from 'react';
import axios from "axios";
import { API_URL } from '../../consts';

const EditPlace = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const placeId = useParams().placeId;
    const [version, setVersion] = useState("");
    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = React.useState(false);

    const [area, setArea] = useState("");
    const [areaValid, setAreaValid] = useState(true);
    const [areaError, setAreaError] = useState("");

    const fetchData = async () => {
        axios.get(`${API_URL}/places/place/${placeId}`,{
            headers: {
                'Authorization': token
            }
        }).then((response) => {
            setVersion(response.data.version);
            localStorage.setItem("etag", response.headers["etag"]);
            setArea(response.data.area);
        }).catch((error) => {
            if (error.response.status === 403) {
                setAuthorizationErrorOpen(true);
            }
        });
    }

    useEffect(() => {
        fetchData();
    }, []);

    const handleAreaChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const regex = /^[0-9]+(?:\.[0-9]+)?$/;
        if (validator.matches(event.target.value, regex)) {
            setArea(event.target.value);
            setAreaValid(true);
            setAreaError("");
        } else {
            setAreaError(t("editPlace.edit_place_error"));
            setAreaValid(false);
        }
    };

    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleClose = () => {
        setAreaError("");
        setAreaValid(false);

        setOpen(false);
    };

    const handleSave = () => {
        setConfirmOpen(true);
    };

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
        }
    };

    const handleConfirmSave = () => {  
        if(!areaValid) {
            setOpenSnackbar(true);
            setConfirmOpen(false);
            return;
        }

        const editPlaceDTO = {
            area : area,
            version: version
        }
        axios.patch(`${API_URL}/places/place/${placeId}`,
        editPlaceDTO, {
            headers: {
                'Authorization': token,
                'If-Match': localStorage.getItem("etag")
            }
        }).then((response) => {
            window.location.reload();
        }).catch((error) => {
            setConfirmOpen(false); 
            setOpenSnackbar(true);
            return;
        });

        setConfirmOpen(false); 
        setOpen(false);
    };

    const handleCloseSnackbar = () => { 
        setOpenSnackbar(false);
    };

    setTimeout(handleCloseSnackbar, 10000);
    


    return (
        <div>
            <div className="row">
                <div className="col-12">
                    <ButtonGroup>
                        <Button variant="contained" color="primary" onClick={handleClickOpen}>
                            {t('editPlace.edit_place_button')}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>


            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{t('editPlace.edit_place_title')}</DialogTitle>
                <DialogContent>
                <DialogContentText>{t('editPlace.edit_place_description')}</DialogContentText>
                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label={t('editPlace.edit_place_text_field_area')+"*"}
                    fullWidth
                    variant="standard"
                    defaultValue={parseFloat(area).toFixed(2)}
                    onChange={handleAreaChange}/>
                    <DialogContentText style={{ fontSize: "13px", color: "red" }}>
                        {areaError}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSave}>{t('confirm.save')}</Button>
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={confirmOpen} onClose={handleConfirmCancel}>
                <DialogTitle>{t('editPlace.edit_place_title')}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {t('editPlace.edit_place_confirm')}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleConfirmSave}>{t('confirm.yes')}</Button>
                    <Button onClick={handleConfirmCancel}>{t('confirm.no')}</Button>
                </DialogActions>
            </Dialog>


            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('personal_data.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>

            <Snackbar open={openSnackbar} onClose={handleCloseSnackbar}>
                <SnackbarContent 
                message={t('editPlace.edit_place_failed')}/>
            </Snackbar>
        </div>
    );

}

export default EditPlace;