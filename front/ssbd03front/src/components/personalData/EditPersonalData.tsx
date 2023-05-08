import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import NavbarPanel from '../navigation/NavbarPanel';
import { TextField } from '@mui/material';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import axios from 'axios'; 

const GET_DATA_URL = 'http://localhost:8080/api/data';

export default function EditPersonalData() {
  const [open, setOpen] = React.useState(false);
  var [name, setName] = React.useState('');
  var [surname, setSurname] = React.useState('');

  axios.get('/api/data', {
    headers: {
      Authorization: 'Bearer ' + ""
    },
    params: {
      limit: 10,
      offset: 0
    }
  })
  .then(response => {
    // handle response data
  })
  .catch(error => {
    // handle error
  });
  

  const handleSumbit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
}
  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    name = event.target.value;
  };

  const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    surname = event.target.value;   
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setOpen(false);
    }
  };

  return (
    <div>
      <NavbarPanel/>
      <div>
      <Button onClick={handleClickOpen} variant="contained">Edytuj dane</Button>
      </div>
      <Dialog disableEscapeKeyDown open={open} onClose={handleClose}>
        <DialogTitle>Wypełnij formularz edycji danych osobowych</DialogTitle>
        <DialogContent>
          <Box component="form" sx={{ display: 'flex', flexWrap: 'wrap' }}>
            <form onSubmit={handleSumbit}>
              <List component="nav" aria-label="mailbox folders">
                <ListItem>
                  <div className="form-group" onChange={handleNameChange}>
                    <TextField
                      id="outlined-helperText"
                      label="Imię"
                      defaultValue="Default Value"
                      helperText="Wprowadź imię o maksymalniej ilości znaków 32"
                    />
                  </div>
                </ListItem>
                <ListItem>
                  <div className="form-group" onChange={handleSurnameChange}>
                    <TextField
                      id="outlined-helperText"
                      label="Nazwisko"
                      defaultValue="Default Value"
                      helperText="Wprowadź nazwisko o maksymalniej ilości znaków 32"
                    />
                  </div>
                </ListItem>
              </List>
            </form>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleClose}>Ok</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}