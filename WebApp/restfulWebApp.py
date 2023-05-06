from apispec import APISpec
from apispec.ext.marshmallow import MarshmallowPlugin
from apispec_webframeworks.flask import FlaskPlugin
from marshmallow import Schema, fields
from flask import Flask, abort, request, make_response, jsonify
from pprint import pprint
import json
import time

import yaml


class DemoParameter(Schema):
    gist_id = fields.Int()


class DemoSchema(Schema):
    id = fields.Int()
    content = fields.Str()


spec = APISpec(
    title="Demo API",
    version="1.0.0",
    openapi_version="3.0.0",
    info=dict(
        description="Demo API",
        version="1.0.0-oas3",
        contact=dict(
            email="av3089@columbia.edu"
        ),
        license=dict(
            name="Apache 2.0",
            url='http://www.apache.org/licenses/LICENSE-2.0.html'
        )
    ),
    servers=[
        dict(
            description="Local server",
            url="http://localhost:5000"
        )
    ],
    tags=[
        dict(
            name="Sample Tag",
            description="Sample Tag Description"
        )
    ],
    plugins=[FlaskPlugin(), MarshmallowPlugin()],
)

spec.components.schema("Demo", schema=DemoSchema)
app = Flask(__name__)


@app.route("/demo/<gist_id>", methods=["GET"])
def my_route(gist_id):
    """Gist detail view.
    ---
    get:
      parameters:
      - in: path
        schema: DemoParameter
      responses:
        200:
          description: Successful Response
    """
    # Construct the response object
    time.sleep(250/1000)
    response = {
        'id': gist_id,
        'content': 'This is the content for gist {}'.format(gist_id)
    }

    # Return the response as JSON
    return jsonify(response)


@app.route("/hello", methods=["GET"])
def hello():
    """Gist detail view.
    ---
    get:
      parameters:
      - name: name
        description: tell me your name
        in: query
        required: true
        schema: 
          type: string
      responses:
        200:
          description: Successful Response
        400:
          description: Didn't supply name
        default:
         description: Other responses 
    """
    args = request.args
    if "name" not in args:
        return "Send name, please", 400
    name = args.get('name')
    # Construct the response object
    response = ['hello ' + name]

    # Return the response as JSON
    return jsonify(response)


with app.test_request_context():
    spec.path(view=my_route)
    spec.path(view=hello)

with open('openapi.yaml', 'w') as f:
    yaml.dump(yaml.load(spec.to_yaml(), Loader=yaml.FullLoader), f)

# pprint(spec.to_dict())
# print(spec.to_yaml())

if __name__ == '__main__':
    app.run(debug=True)
