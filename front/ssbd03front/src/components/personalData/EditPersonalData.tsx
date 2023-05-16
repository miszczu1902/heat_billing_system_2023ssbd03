import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { TextField } from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios'; 
import validator from "validator";
import { API_URL } from '../../consts';
import {useCookies} from 'react-cookie';
import { useState, useEffect } from 'react';
import {useTranslation} from "react-i18next";


const EditPersonalData = () => {
  const {t, i18n} = useTranslation();
  const [cookies, setCookie] = useCookies(["token"]);
  const token = "Bearer " + cookies.token;
  const [open, setOpen] = React.useState(false);
  const [confirmOpen, setConfirmOpen] = React.useState(false);

  const [name, setName] = React.useState("");
  const [surname, setSurname] = React.useState("");

  const [nameError, setNameError] = React.useState("");
  const [surnameError, setSurnameError] = React.useState("");
  const [dataError, setDataError] = React.useState("");

  const [validData, setValidData] = React.useState(false);

  const [successOpen, setSuccessOpen] = React.useState(false);
  const [errorOpen, setErrorOpen] = React.useState(false);

  useEffect(() => {
    const fetchData = async () => {
      const response = await axios.get(`${API_URL}/accounts/self/personal-data`, {
        headers: {
          Authorization: token
        }
      })
      .then(response => {
        setName(response.data.firstName.toString());
        setSurname(response.data.surname.toString());
      });
  };
  fetchData();
  }, []);

  const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
}

  const validateData = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (nameError === "" && surnameError === "" && name !== "" && surname !== "" && event.target.value.length > 0) {
      setValidData(true);
    } else {
      setValidData(false);
    }
  }

  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setName(event.target.value)
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
      setNameError("");
      validateData(event);
    } else {
      setNameError(t('personal_data.name_error'));
      validateData(event);
    }
  };

  const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => { 
    setSurname(event.target.value);
    if (validator.isAlpha(event.target.value) && event.target.value.length <= 32 && event.target.value.length > 0) {
      setSurnameError("");
      validateData(event);
    } else {
      setSurnameError(t('personal_data.surname_error'));
      validateData(event); 
    } 
  };

  const handleClickOpen = () => {
    axios.get(`${API_URL}/accounts/self/personal-data`, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    })
    .then(response => {
        setName(response.data.firstName.toString());
        setSurname(response.data.surname.toString());
    });
    setOpen(true);
  };

  const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setOpen(false);
    }
  };

  const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setConfirmOpen(false);
    }
  }

  const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setConfirmOpen(false);
    }
    const personalDataDTO = {
      firstName: name.toString(),
      surname: surname.toString()
    }

    if(nameError === "" && surnameError === "") {
      axios.patch(`${API_URL}/accounts/self/personal-data`, 
        personalDataDTO, {
         headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json'
        }, 
      })
      .then(response => {
        setSuccessOpen(true);
      })
      .catch(error => {
        setErrorOpen(true);
      });
    }
    handleClose(event, reason);
  }

  const handleConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if(validData) {
      setDataError("");
      setConfirmOpen(true);
    } else {
      setDataError(t('personal_data.data_error'));
    }
  }
  
  const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setSuccessOpen(false);
    }
  }

  const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setErrorOpen(false);
    }
  };

  return (
    <div>
      <div>
      <Button onClick={handleClickOpen} variant="contained">{t('personal_data.edit_data')}</Button>
      </div>
      <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
        <DialogTitle>{t('personal_data.dialog_title')}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexWrap: 'wrap' }}>
            <form onSubmit={handleSumbit}>
              <List component="nav" aria-label="mailbox folders">
                <ListItem>
                  <div className="form-group" onChange={handleNameChange}>
                    <TextField
                      id="outlined-helperText"
                      label={t('personal_data.name')}
                      defaultValue= {name}
                      helperText={t('personal_data.name_helper_text')}
                    />
                    <div className="form-group">
                      {nameError}
                    </div>
                  </div>
                </ListItem>
                <ListItem>
                  <div className="form-group" onChange={handleSurnameChange}>
                    <TextField
                      id="outlined-helperText"
                      label={t('personal_data.surname')}
                      defaultValue= {surname}
                      helperText={t('personal_data.name_helper_text')}
                    />
                    <div className="form-group">
                      {surnameError}
                    </div>
                  </div>
                </ListItem>
              </List>
              <div className="form-group">
                      {dataError}
              </div>
            </form>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
          <Button onClick={handleConfirm} disabled={!validData}>{t('confirm.ok')}</Button>
        </DialogActions>
      </Dialog>

      <Dialog disableEscapeKeyDown open={confirmOpen} onClose={handleConfirmClose}>
        <DialogTitle>{t('personal_data.confirm')}</DialogTitle>
        <DialogActions>
          <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
          <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
        </DialogActions>
      </Dialog>

      <Dialog disableEscapeKeyDown open={successOpen}>
        <DialogTitle>{t('personal_data.success')}</DialogTitle>
        <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
      </Dialog> 

      <Dialog disableEscapeKeyDown open={errorOpen}>
        <DialogTitle>{t('personal_data.error')}</DialogTitle>
        <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
      </Dialog>
    </div>
  );
}

export default EditPersonalData;