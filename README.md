# CodeSnippets
An [IntelliJ](https://www.jetbrains.com/idea/) plugin containing various snippets of code for the Programmieren exam.

## Usage

### Build

- Open the project in InteliJ and build it (Build>Build Project)
- Make it ready for deployment, build jar archive (Build>Prepare Plugin 'CodeSnippets' for Deploment)

### Import it into IntelliJ

- Import from local Disk (File>Settings>Plugins>Install Plugin from Disk)

## Development

### Custom actions

- Register new group in _plugin.xml_ (example in the code)
- Add actions to the group and connect them to a new class, that implements _actionPerformed_-method (copy it from one of the already existing classes)
- (Implement the _update_-method for further configurations)
- Distinguish the different actions by their _text_-attribute (returned by the _toString_-method)
