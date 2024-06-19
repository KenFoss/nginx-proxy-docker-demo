import logo from '../logo.svg';

function Login () {
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      // Response type is important here, getting an access code (setting it to "code") is more secure but then requires the extra step
      // Of getting the token with this code.
      // This is recommended when returning the token to your frontend as this is client facing, and the user can now gain access to the token
      // However since we are redirecting to the backend getting a token is
      const params = new URLSearchParams({
        client_id: '1062665072865-8dhbc8sil84rgpmsmoleou1v86244qt0.apps.googleusercontent.com',
        redirect_uri: 'http://localhost:3000/dashboard',
        scope: 'email profile openid',
        response_type: 'id_token',
        prompt: 'consent'
      });

      window.location.href = `https://accounts.google.com/o/oauth2/auth?${params.toString()}`;
    } catch (error) {
      console.error(`Error initiating OAuth: ${error}`);
    }
  }

  return(
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <h1>Welcome to the OAuth Demo!</h1>
        <form onSubmit={handleSubmit}>
          <input type='submit' value='Login' />
        </form>
      </header>
    </div>
  )
}

export default Login;