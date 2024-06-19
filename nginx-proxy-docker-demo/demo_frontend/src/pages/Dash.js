import {useState, useEffect} from 'react';

const Dash = () => {

  let [idToken, setIdToken] = useState('');
  let [fetchCount, setFetchCount] = useState(0);
  let [jwToken, setjwToken] = useState('');
  let [authenticated, setAuthenticated] = useState(false);
  let [helloUser, setHelloUser] = useState(null);

  const isAuthenticated = async () => {
    try{
      const response = await fetch('http://localhost:8090/oauth2/is-authenticated', {
        method: 'GET',
        headers: {
          'Authorization': localStorage.getItem("DemoAuthToken"),
          'Content-Type': 'application/json'
        }
      });
      if (response.ok) {
        const data = await response.json();
        console.log(`data is ${data}`);
        setAuthenticated(true);
      } else {
        setAuthenticated(false);
      }
    } catch (error) {
      console.error(`Error fetching isAuthenticated: ${error}`);
    }
  
  }


  // When the user is redirected here... get the token 
  useEffect(() => {
    const fetchData = async () => {
       
      // This will set authenticated to true if the token is valid
      // If the token is not valid, attempt to fetch a new token
      await isAuthenticated();

      if (!authenticated) {  
        try {
          var fragmentString = window.location.href.split('#')[1];
          let paramsList = fragmentString.split('&');
          let params = {};
          paramsList.forEach(entry => {
            let [key, value] = entry.split('=');
            params[key] = value;
          });
    
          const response = await fetch('http://localhost:8090/oauth2/google', {
            method: 'POST',
            headers: {
              'Authorization': `${params['id_token']}`,
              'Content-Type': 'application/json'
            }
          });
          
          if (response.ok) {
            const data = await response.json();
            setjwToken(data['jwToken']);
            localStorage.setItem("DemoAuthToken", data['jwToken'])
            setAuthenticated(true);
          } else {
            setAuthenticated(false);
          }
          // Handle the response data as needed
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      }
    }
    
    fetchData();
  }, []);

  const handleClick = () => {
    const fetchData = async () => {
      try {

        const response = await fetch('http://localhost:8090/hello', {
          method: 'GET',
          headers: {
            'Authorization': jwToken,
            'Content-Type': 'application/json'
          }
        });
  
        const data = await response.json();
        console.log(data);
        if ('message' in data) {
          setHelloUser(data['message']);
        }

        // Handle the response data as needed
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
  
    fetchData();
  }

  const handleClickNoJwt = () => {
    const fetchData = async () => {
      try {

        const response = await fetch('http://localhost:8090/hello', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });
  
        const data = await response.json();

        // Handle the response data as needed
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
  
    fetchData();
  }


  return(
    <div>
      {authenticated ? (
        <h1>You are Logged In!</h1>
      ) : (
        <h1>Authentication Failed!</h1>
      )}

      {helloUser !== null ? (
        <h4>{helloUser}</h4>
      ) : null}
      <button onClick={() => handleClick()}> request with jwt </button>
      <button onClick={() => handleClickNoJwt()}> request without jwt </button>
    </div>
  )
}

export default Dash;