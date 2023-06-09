import validator from "validator";
import {useTranslation} from "react-i18next";
import {
    Button,
    ButtonGroup,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Snackbar,
    SnackbarContent,
    TextField
} from '@mui/material';
import React, {useEffect, useState} from 'react';
import axios from "axios";
import {API_URL} from '../../consts';


const ChangePlaceOwner = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const {t, i18n} = useTranslation();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [placeId, setPlaceId] = useState("0");
    const [version, setVersion] = useState("");

    const [username, setUsername] = useState("");
    const [usernameValid, setUsernameValid] = useState(false);
    const [usernameError, setUsernameError] = useState("");

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = React.useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        axios.get(`${API_URL}/places/place/${placeId}`, {
            headers: {
                'Authorization': token
            }
        }).then((response) => {
            setVersion(response.data.version);
            localStorage.setItem("etag", response.headers["etag"]);
            setUsername(response.data.username);
        }).catch((error) => {
            if (error.response.status === 403) {
                setAuthorizationErrorOpen(true);
            }
        });
    }

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setUsernameError("");
        setUsernameValid(false);
        setOpen(false);
    };

    const handleSave = () => {
        setConfirmOpen(true);
    };

    const handleConfirmSave = () => {
        if (!usernameValid) {
            setOpenSnackbar(true);
            setConfirmOpen(false);
            return;
        }

        const modifyPlaceOwnerDTO = {
            username: username,
            version: version
        }
        axios.patch(`${API_URL}/places/owner/${placeId}`,
            modifyPlaceOwnerDTO, {
                headers: {
                    'Authorization': token,
                    'If-Match': localStorage.getItem("etag")
                }
            }).then((response) => {
        }).catch((error) => {
            setConfirmOpen(false);
            setOpenSnackbar(true);
            return;
        });

        setConfirmOpen(false);
        setOpen(false);
    };

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handleUsernameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const regex = /^[a-zA-Z0-9_]{6,16}$/;
        if (validator.matches(event.target.value, regex)) {
            setUsername(event.target.value);
            setUsernameValid(true);
            setUsernameError("");
        } else {
            setUsernameValid(false);
            setUsernameError(t('modifyPlaceOwner.enter_username_error'));
        }
    }

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
            handleConfirmCancel();
        }
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    setTimeout(handleCloseSnackbar, 10000);

    return (
        <div className="container">
            <div className="row">
                <div className="col-12">
                    <ButtonGroup>
                        <Button variant="contained" color="primary" onClick={handleClickOpen}>
                            {t('modifyPlaceOwner.modify_place_owner_button')}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{t('modifyPlaceOwner.modify_place_owner_title')}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {t('modifyPlaceOwner.modify_place_owner_message')}
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label={t('modifyPlaceOwner.enter_first_name')}
                        fullWidth
                        variant="standard"
                        onChange={handleUsernameChange}
                    />
                    <DialogContentText style={{fontSize: "13px", color: "red"}}>
                        {usernameError}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSave}>{t('confirm.save')}</Button>
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={confirmOpen} onClose={handleConfirmCancel}>
                <DialogTitle>{t('modifyPlaceOwner.modify_place_owner_title')}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {t('modifyPlaceOwner.modify_place_owner_confirm')}
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
                    message={t('modifyPlaceOwner.modify_place_owner_failed')}/>
            </Snackbar>
        </div>
    );
}

export default ChangePlaceOwner;