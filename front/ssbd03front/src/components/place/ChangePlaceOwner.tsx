import {useTranslation} from "react-i18next";
import {
    Button,
    ButtonGroup,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    FormControl,
    FormHelperText,
    InputLabel,
    List,
    MenuItem,
    Select,
    SelectChangeEvent,
    Snackbar,
    Alert
} from '@mui/material';
import React, {useEffect, useState} from 'react';
import axios from "axios";
import {API_URL} from '../../consts';
import ListItem from "@mui/material/ListItem";
import {Account} from "../../types/account";
import {useParams} from "react-router-dom";


const ChangePlaceOwner = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const {t, i18n} = useTranslation();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [version, setVersion] = useState("");
    const placeId = useParams().placeId;

    const [username, setUsername] = useState("");
    const [message, setMessage] = useState("");

    const [openSnackbar, setOpenSnackbar] = React.useState(false);
    const [accounts, setAccounts] = useState<Account[]>([]);

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
            setMessage(error.response.data.message);
        });
        axios.get(`${API_URL}/buildings/owners`, {
            headers: {
                Authorization: token
            }
        }).then(response => {
            setAccounts(response.data);
        });
    }

    const handleOwnerChange = (event: SelectChangeEvent) => {
        setUsername(event.target.value);
    };
    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSave = () => {
        setConfirmOpen(true);
    };

    const handleConfirmSave = () => {

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
            //window.location.reload()();
        }).catch((error) => {
            setMessage(error.response.data.message);
            setOpenSnackbar(true);
        });

        setConfirmOpen(false);
        setOpen(false);
    };

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

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
                    <List>
                        <ListItem>
                            <div className="form-group">
                                <FormControl>
                                    <InputLabel id="owner-select-label">{t('place.ownerId')+"*"}</InputLabel>
                                    <Select
                                        labelId="owner-select-label"
                                        id="owner-select"
                                        value={username}
                                        onChange={handleOwnerChange}
                                        fullWidth>
                                        {accounts.map((account) => (
                                            <MenuItem key={account.username} value={account.username}>
                                                {account.username}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                    <FormHelperText>{t('place.ownerIdInfo')}</FormHelperText>
                                </FormControl>
                            </div>
                        </ListItem>
                    </List>
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

            <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
                <Alert onClose={handleCloseSnackbar} severity="error" sx={{width: '100%'}}>
                    {t(message)}
                </Alert>
            </Snackbar>
        </div>
    );
}

export default ChangePlaceOwner;