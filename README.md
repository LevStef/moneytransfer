# moneytransfer
Demo of REST service for money transfers


In order to run the tests, you must currently launch the app manually first. 
There is currently a bug where the newAccountShouldHaveZeroBalance test seems to be hitting POST /account/balance,
despite the request method being set to GET, which is making the test fail.

TODO: 
 - fix test newAccountShouldHaveZeroBalance
 - find better way to order tests than lexical order
 - add good errors for all cases that currently result in 500 internal server error messages
 - create script so service can be started/stopped from within test suite
