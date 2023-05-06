from flask import Flask, request
from flask_rest_api import Api, Blueprint, doc, swagger

app = Flask(__name__)

# Create a new Flask blueprint for our API endpoints
api_bp = Blueprint('api', __name__)
api = Api(api_bp)

# Define a simple Flask route that takes a name parameter and returns a greeting


@api.route('/greet/<string:name>')
@api.doc(params={'name': {'description': 'The name of the person to greet'}})
@api.doc(responses={200: 'Success', 400: 'Bad Request'})
def greet(name):
    if not name:
        return {'error': 'Name parameter is required'}, 400
    return {'message': f'Hello, {name}!'}

# Define a route for adding two numbers


@api.route('/add')
@api.doc(params={'a': {'description': 'The first number to add'}, 'b': {'description': 'The second number to add'}})
@api.doc(responses={200: 'Success', 400: 'Bad Request'})
def add():
    a = request.args.get('a')
    b = request.args.get('b')
    if not a or not b:
        return {'error': 'Both a and b parameters are required'}, 400
    try:
        result = int(a) + int(b)
    except ValueError:
        return {'error': 'Both a and b must be integers'}, 400
    return {'result': result}

# Define a route for subtracting two numbers


@api.route('/subtract')
@api.doc(params={'a': {'description': 'The number to subtract from'}, 'b': {'description': 'The number to subtract'}})
@api.doc(responses={200: 'Success', 400: 'Bad Request'})
def subtract():
    a = request.args.get('a')
    b = request.args.get('b')
    if not a or not b:
        return {'error': 'Both a and b parameters are required'}, 400
    try:
        result = int(a) - int(b)
    except ValueError:
        return {'error': 'Both a and b must be integers'}, 400
    return {'result': result}


# Register the blueprint with our Flask app
app.register_blueprint(api_bp)

# Define a route to serve the generated OpenAPI specification


@app.route('/openapi')
def serve_swagger():
    return swagger(app)


if __name__ == '__main__':
    app.run()
