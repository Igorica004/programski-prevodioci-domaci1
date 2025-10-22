{
  description = "Java flake";
    inputs = {
      nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
      intellij-flake.url = "path:/home/Igor/Programiranje/flakes/intellij";
    };
    outputs = { self, nixpkgs, intellij-flake }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in {
      devShells.${system}.default = pkgs.mkShell {
        packages = [
          intellij-flake.intellij
          pkgs.jdk21_headless
        ];
      };
    };
    
  }
