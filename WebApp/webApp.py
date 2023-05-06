from flask import Flask, jsonify
from flasgger import Swagger, swag_from

app = Flask(__name__)
swagger = Swagger(app)


@app.route('/hello/<string:name>', methods=['GET'])
@swag_from('docs/hello.yml')
def hello(name):
    """Endpoint that returns a personalized greeting.
    ---
    parameters:
      - in: path
        name: name
        type: string
        required: true
        description: The name to greet.
    responses:
      200:
        description: A greeting message
    """
    return jsonify({'message': f'Hello, {name}!'})


if __name__ == '__main__':
    app.run(debug=True)
