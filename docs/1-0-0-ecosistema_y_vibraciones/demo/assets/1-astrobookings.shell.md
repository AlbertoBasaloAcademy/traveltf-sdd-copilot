```shell
mkdir astrobookings
cd astrobookings
git init

# gitignore
echo "node_modules/" > .gitignore

# README.md
echo -e "# AstroBookings \n A REST API and a web application for the fictional space tourism company (AstroBookings).\n Its purpose is to support its employees in their daily operations. Manage the rocket fleet, plan and confirm or cancel launches, as well as record passenger bookings without overbooking. Payments and refunds are processed through a fictional payment gateway.\n It is an MVP, so it does not require authentication or storage features." > README.md

mkdir back
mkdir front
```