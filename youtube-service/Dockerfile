# Use an official Python runtime as a parent image
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements.txt file to the container
COPY requirements.txt .

# Install the Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code to the container
COPY . .

# Expose the port your gRPC server is running on (e.g., 50051)
EXPOSE 50051

# Define the command to run your gRPC server
CMD ["python", "gRPC_server.py"]
